	package ch.vivates.tools.sec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map.Entry;

import javax.sql.DataSource;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLException;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.NameID;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * The Class ExtendedAuthenticationManager.
 * 
 * @author Federico Marmory, Post CH, major development
 * @author Kevin Tippenhauer, Berner Fachhochschule, javadoc
 */
public class ExtendedAuthenticationManager implements AuthenticationManager {
	
	/** The Logger */
	private static final Logger LOG = LoggerFactory.getLogger(ExtendedAuthenticationManager.class);
	
	/** The grace period max value. */
	private final long GRACE_PERIOD_MAX_VALUE = 10000l;
	
	/** The data source. */
	private DataSource dataSource;
	
	/** The grace period. */
	private long gracePeriod;
	
	/** The verify conditions. */
	private boolean verifyConditions;
	
	/** The verify signature. */
	private boolean verifySignature;
	
	/** The keystorepath. */
	private String keystorepath;
	
	/** The keystorepass. */
	private String keystorepass;
	
	/** The keystore aliases. */
	private String keystoreAliases;

	/** The certificate store. */
	private CertificateStore certificateStore;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationManager#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (authentication == null) {
			throw createSOAPFaultException("Access denied: Missing authentication token.", null); 
		}
		
		if (authentication instanceof SAMLPrincipal) {
			return authenticate((SAMLPrincipal)authentication);
		} else if (authentication instanceof UsernamePasswordAuthenticationToken) {
			return authenticate((UsernamePasswordAuthenticationToken)authentication);
		} else if (authentication instanceof TestingAuthenticationToken) {
			authentication.setAuthenticated(true);
			((TestingAuthenticationToken) authentication).setDetails("test-hospital-01");
			return authentication;
		} else {
			throw createSOAPFaultException("Access denied: Unknown authentication token.", null); 
		}
	}
	
	/**
	 * Authenticates a UsernamePasswordAuthenticationToken
	 *
	 * @param authToken the UsernamePasswordAuthenticationToken
	 * @return the Authentication
	 * @throws SOAPFaultException the SOAP fault exception
	 */
	public Authentication authenticate(UsernamePasswordAuthenticationToken authToken) throws SOAPFaultException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.prepareStatement(
							"SELECT community_uid FROM auth_token WHERE token_type='PSWD' AND user_id='" + authToken.getName() + "' AND token_element='"+authToken.getCredentials()+"'");
			results = statement.executeQuery();
			if (!results.first()) {
				MDC.put("hpd.username", "no-auth");
				LOG.info("Authentication failed: access denied - invalid username/password");
				throw createSOAPFaultException("Access denied: invalid username/password!", null);
			} else {
				UsernamePasswordAuthenticationToken grantedAuthentication = new UsernamePasswordAuthenticationToken(authToken.getName(), authToken.getCredentials(), null);
				grantedAuthentication.setDetails(results.getString(1));
				MDC.put("hpd.username", grantedAuthentication.getName());
				LOG.info("Username/Password authentication succeeded!");
				return grantedAuthentication;
			}
		} catch (SQLException e) {
			throw createSOAPFaultException("Internal error: service temporarly unavailable!", e);
		} finally {
			try {
				results.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				LOG.warn("Error releasing connection objects",e);
			}
		}
	}
	
	/**
	 * Authenticates a SAMLPrincipal.
	 *
	 * @param authToken the SAMLPrincipal
	 * @return the Authentication
	 * @throws SOAPFaultException the SOAP fault exception
	 */
	public Authentication authenticate(SAMLPrincipal authToken) throws SOAPFaultException {
		Assertion assertion = authToken.getCredentials();
		
		if (verifyConditions) {
			// Verify time
			Conditions conditions = assertion.getConditions();
			if (conditions == null) {
				throw createSOAPFaultException("SAML Assertion is missing Conditions", null);
			}

			DateTime notBefore = conditions.getNotBefore();
			DateTime notOnOrAfter = conditions.getNotOnOrAfter();

			if (notBefore == null || notOnOrAfter == null) {
				throw createSOAPFaultException("SAML Assertion is missing time conditions", null);
			}

			// We get the milliseconds for the 2 DateTime(s) we have.
			// http://joda-time.sourceforge.net/apidocs/org/joda/time/base/BaseDateTime.html#getMillis%28%29
			// Gets the milliseconds of the datetime instant from the Java epoch
			// of 1970-01-01T00:00:00Z.
			long notBeforeMillis = notBefore.getMillis();
			long notOnOrAfterMillis = notOnOrAfter.getMillis();

			// Next get the current time in Millis
			// http://download.oracle.com/javase/1.5.0/docs/api/java/lang/System.html#currentTimeMillis%28%29
			// the difference, measured in milliseconds, between the current
			// time and midnight, January 1, 1970 UTC.
			long currentMillis = System.currentTimeMillis();

			// OK so now we have ALL dates in the offset from the Java epoch
			// so a simple compare can be done...
			if ((currentMillis < notBeforeMillis - gracePeriod) || (notOnOrAfterMillis < currentMillis)) {
				throw createSOAPFaultException("SAML Assertion is expired", null);
			}
		}

		// verify the signature
		if (verifySignature) {
			verifySignature(assertion.getSignature());
		}
		
		// verify the user is in the database
		try {
			NameID name = assertion.getSubject().getNameID();
			PreparedStatement statement = dataSource.getConnection()
					.prepareStatement(
							"SELECT community_uid FROM auth_token WHERE token_type='SAML' AND user_id='"+ name.getValue() + "' AND token_element='"+name.getSPProvidedID()+"'");
			ResultSet results = statement.executeQuery();
			if (!results.first()) {
				throw createSOAPFaultException("Access denied: invalid username/password!", null);
			} else {
				authToken.setDetails(results.getString(1));
				authToken.setAuthenticated(true);
				MDC.put("hpd.username", authToken.getName());
				LOG.info("SAML authentication succeeded!");
				return authToken;
			}
		} catch (SQLException e) {
			throw createSOAPFaultException("Internal error: service temporarly unavailable!", e);
		}
	}
	
	/**
	 * Initializes the ExtendedAutheticationManager.
	 *
	 * @throws SAMLException the SAML exception
	 */
	public void init() throws SAMLException {
		try {
			DefaultBootstrap.bootstrap();
		} catch (ConfigurationException e) {
			throw new SAMLException("OpenSAML could not be initialized.", e);
		}
		
		if(StringUtils.isBlank(keystorepath)  || StringUtils.isBlank(keystorepass)) {
			throw new BeanInitializationException("Missing keystore information.");
		}
		certificateStore = new CertificateStore(keystorepath, keystorepass, keystoreAliases);
	}
	
	/**
	 * Creates the soap fault exception.
	 *
	 * @param faultString the fault string
	 * @param cause the cause as Exception
	 * @return the SOAP fault exception
	 */
	private SOAPFaultException createSOAPFaultException(String faultString, Exception cause) {
		LOG.debug(faultString, cause);
		try {
			SOAPFault fault = SOAPFactory.newInstance().createFault();
			fault.setFaultString(faultString);
			fault.setFaultCode(new QName(SOAPConstants.URI_NS_SOAP_ENVELOPE, "Authentication"));
			return new SOAPFaultException(fault);
		} catch (SOAPException e) {
			throw new RuntimeException("Error creating SOAP Fault message, faultString: " + faultString, cause);
		}
	}
	
	/**
	 * Verifies a signature.
	 *
	 * @param signature the Signature
	 * @throws SOAPFaultException the SOAP fault exception
	 */
	private void verifySignature(Signature signature) throws SOAPFaultException {
		// step #1
		if (signature == null) {
			throw createSOAPFaultException("SAML-Assertion is not signed, its signature is missing.", null);
		}

		// step #2
		try {
			SAMLSignatureProfileValidator samlSignatureProfileValidator = new SAMLSignatureProfileValidator();

			samlSignatureProfileValidator.validate(signature);
		} catch (ValidationException e) {
			throw createSOAPFaultException("Signature of the SAML-assertion does not conform to the SAML-profile of XML Signature.", e);
		}

		// step #3
		boolean isSignatureValid = false;

		for (Entry<String, Credential> credentialEntry : certificateStore.getCredentials().entrySet()) {
			try {
				SignatureValidator signatureValidator = new SignatureValidator(credentialEntry.getValue());

				signatureValidator.validate(signature);

				isSignatureValid = true;

				break;
			} catch (ValidationException vExcp) {
				// this is an expected behavior, as we need to find out, if
				// there is a certificate, that
				// contains such a public key, that was used to create the
				// signature with.
				if (LOG.isDebugEnabled()) {
					LOG.debug("Validation of signature '" + signature + "' failed using the credential identified by alias '"
							+ credentialEntry.getKey() + "'.");
				}
			}
		}

		if (!isSignatureValid) {
			throw createSOAPFaultException("Signature of the SAML-assertion is not signed with any of the available credentials.", null);
		}
	}

	/**
	 * Sets the data source.
	 *
	 * @param dataSource the new DataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Sets the grace period.
	 *
	 * @param gracePeriod the new grace period as long
	 */
	public void setGracePeriod(long gracePeriod) {
		this.gracePeriod = Math.min(gracePeriod, GRACE_PERIOD_MAX_VALUE);
	}

	/**
	 * Sets if the Conditions of the Authentication should be verified.
	 *
	 * @param verifyConditions true, if the conditions should be verified.
	 */
	public void setVerifyConditions(boolean verifyConditions) {
		this.verifyConditions = verifyConditions;
	}

	/**
	 * Sets if the Signature of the Authentication should be verified.
	 *
	 * @param verifySignature true, if the signature should be verified.
	 */
	public void setVerifySignature(boolean verifySignature) {
		this.verifySignature = verifySignature;
	}

	/**
	 * Sets the keystorepath.
	 *
	 * @param keystorepath the new keystorepath
	 */
	public void setKeystorepath(String keystorepath) {
		this.keystorepath = keystorepath;
	}

	/**
	 * Sets the keystorepass.
	 *
	 * @param keystorepass the new keystorepass
	 */
	public void setKeystorepass(String keystorepass) {
		this.keystorepass = keystorepass;
	}

	/**
	 * Sets the keystore aliases.
	 *
	 * @param keystoreAliases the new keystore aliases
	 */
	public void setKeystoreAliases(String keystoreAliases) {
		this.keystoreAliases = keystoreAliases;
	}

}
