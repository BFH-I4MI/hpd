	package ch.vivates.tools.sec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.opensaml.common.SAMLException;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.NameID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * The Class ExtendedAuthenticationManager.
 * 
 * @author Federico Marmory, Post CH, major development
 * @author Kevin Tippenhauer, Berner Fachhochschule, javadoc, adaption to SamlHelper
 */
public class ExtendedAuthenticationManager implements AuthenticationManager {
	
	/** The Logger */
	private static final Logger LOG = LoggerFactory.getLogger(ExtendedAuthenticationManager.class);
	
	/** The data source. */
	private DataSource dataSource;
	
	/** The saml helper. */
	private SamlHelper samlHelper;
	
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
	 * @throws SAMLException the saml exception
	 */
	public Authentication authenticate(SAMLPrincipal authToken) throws SOAPFaultException {
		Assertion assertion = authToken.getCredentials();
		try {
			samlHelper.verify(assertion);
		} catch (SAMLException samlException) {
			throw createSOAPFaultException("Internal error: service temporarly unavailable!", samlException);
		}
		
		// verify the user is in the database
		try {
			NameID name = assertion.getSubject().getNameID();
			PreparedStatement statement = dataSource.getConnection()
					.prepareStatement(
							"SELECT community_uid FROM auth_token WHERE token_type='SAML' AND user_id='"+ name.getValue() + "' AND token_element='"+name.getSPProvidedID()+"'");
			
			ResultSet results = statement.executeQuery();
			if (!results.first()) {
				LOG.info("Name: " + name.getValue() + " token: " + name.getSPProvidedID());
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
	 * Sets the data source.
	 *
	 * @param dataSource the new DataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * Sets the saml helper.
	 *
	 * @param samlHelper the new SamlHelper.
	 */
	public void setSamlHelper(SamlHelper samlHelper) {
		this.samlHelper = samlHelper;
	}

}
