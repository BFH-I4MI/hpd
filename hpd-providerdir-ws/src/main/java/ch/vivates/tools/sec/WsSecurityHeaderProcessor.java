package ch.vivates.tools.sec;

import java.io.StringWriter;
import java.security.Principal;

import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.opensaml.common.SAMLException;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.ws.wssecurity.WSSecurityConstants;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ws.soap.SoapHeaderElement;
import org.w3c.dom.Element;

/**
 * The Class WsSecurityHeaderProcessor processes the web service security header.
 * 
 * @author Federico Marmory, Post CH, major development
 * @author Kevin Tippenhauer, Berner Fachhochschule, javadoc
 */
public class WsSecurityHeaderProcessor implements Processor {

	/** The Logger */
	private static final Logger LOG = LoggerFactory.getLogger(WsSecurityHeaderProcessor.class);

	/** The debug enabled trigger. */
	private boolean debugEnabled;

	/* (non-Javadoc)
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		// Extract the user info from the SAML and add it to the exchange
		Principal principal = null;
		MDC.remove("hpd.username");
		try {
			principal = extractPrincipal((SoapHeaderElement) exchange.getIn().getHeader("Security"));
		} catch (Exception e) {
			throw createSOAPFaultException("Access denied: missing or invalid security header.", e);
		}

		Subject subject = new Subject();
		subject.getPrincipals().add(principal);

		exchange.getIn().setHeader(Exchange.AUTHENTICATION, subject);
		String principalName = principal.getName();
		exchange.getIn().setHeader("principal", (principalName != null && !principalName.isEmpty() ? principalName : "unknown"));
	}

	/**
	 * Extracts the principal from a SoapHeaderElement.
	 *
	 * @param securityHeader the security header
	 * @return the principal
	 * @throws Exception the exception
	 */
	private Principal extractPrincipal(SoapHeaderElement securityHeader) throws Exception {
		if (securityHeader != null) {
			logSecurityHeader(securityHeader);
			Element securityElement = (Element) ((DOMSource) securityHeader.getSource()).getNode();
			Element assertionElement = (Element) securityElement.getElementsByTagNameNS(SAMLConstants.SAML20_NS, "Assertion").item(0);
			if (assertionElement != null) {
				Unmarshaller unmarshaller = Configuration.getUnmarshallerFactory().getUnmarshaller(assertionElement);
				if (unmarshaller == null) {
					throw new SAMLException("Unable to retrieve unmarshaller by DOM Element");
				}

				try {
					Assertion assertion = (Assertion) unmarshaller.unmarshall(assertionElement);
					return new SAMLPrincipal(assertion);
				} catch (UnmarshallingException ex) {
					throw new SAMLException("Could not unmarshall SAML Assertion", ex);
				}

			} else {
				Element usernameTokenElement = (Element) securityElement.getElementsByTagNameNS(WSSecurityConstants.WSSE_NS,
						"UsernameToken").item(0);
				if (usernameTokenElement != null) {
					Element username = (Element) usernameTokenElement.getElementsByTagNameNS(WSSecurityConstants.WSSE_NS, "Username").item(0);
					Element password = (Element) usernameTokenElement.getElementsByTagNameNS(WSSecurityConstants.WSSE_NS, "Password").item(0);
					return new UsernamePasswordAuthenticationToken(username.getTextContent(), password.getTextContent());
				}
			}
		} else if (debugEnabled) {
			// Debug mode user
			return new TestingAuthenticationToken("Test User", "----");
		}
		throw createSOAPFaultException("Access denied: Unable to detect a valid authentication token.", null);
	}

	/**
	 * Creates the soap fault exception.
	 *
	 * @param faultString the fault string
	 * @param cause the cause
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
			throw new RuntimeException("Error creating SOAP Fault message, faultString: " + faultString);
		}
	}

	/**
	 * Logs the security header.
	 *
	 * @param securityHeader the security header
	 */
	private void logSecurityHeader(SoapHeaderElement securityHeader) {
		try {
			if (LOG.isDebugEnabled()) {
				StringWriter sw = new StringWriter();
				TransformerFactory tf = TransformerFactory.newInstance();
				Transformer transformer;
				transformer = tf.newTransformer();
				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
				transformer.setOutputProperty(OutputKeys.METHOD, "xml");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				transformer.transform(securityHeader.getSource(), new StreamResult(sw));
				LOG.debug("Security header: " + sw.toString());
			}
		} catch (TransformerException e) {
			LOG.error("Failed to transform the security header to log.");
		}
	}

	/**
	 * Sets the debug enabled.
	 *
	 * @param debugEnabled true, to enable debug mode.
	 */
	public void setDebugEnabled(boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

}
