package ch.bfh.i4mi.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.opensaml.ws.wssecurity.WSSecurityConstants;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import ch.vivates.ihe.hpd.pid.model.cs.BatchRequest;
import ch.vivates.ihe.hpd.pid.model.cs.BatchResponse;

/**
 * The Class HPDClient represents a client for the HPD web service.
 */
public class HPDClient extends WebServiceGatewaySupport {
	
	/** The username. */
	private String username;
	
	/** The password. */
	private String password;
	
	
	/**
	 * Instantiates a new HPD client.
	 * 
	 * @throws Exception when username or password is NULL or empty.
	 */
	public HPDClient(String aUsername, String aPassword) throws Exception {
		this.setUsername(aUsername);
		this.setPassword(aPassword);
	}

	/**
	 * Returns the BatchResponse for the Request if possible otherwise an
	 * exception will be thrown.
	 *
	 * @param batchRequest
	 *            the batch request
	 * @return the batch response
	 * @throws SOAPException
	 *             the SOAP exception
	 */
	public BatchResponse getBatchResponse(final BatchRequest batchRequest)
			throws SOAPException {

		SaajSoapMessageFactory saajSoapMessageFactory = 
				new SaajSoapMessageFactory();
		saajSoapMessageFactory.setSoapVersion(SoapVersion.SOAP_12);
		MessageFactory mf12 = MessageFactory
				.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		saajSoapMessageFactory.setMessageFactory(mf12);

		getWebServiceTemplate().setMessageFactory(saajSoapMessageFactory);

		JAXBElement<BatchRequest> jaxbBatchRequest = 
				new JAXBElement<BatchRequest>(
				new QName("urn:batchRequest"), BatchRequest.class,
				batchRequest);

		SoapActionCallback soapActionCallback = new SoapActionCallback(
				"http://147.87.117.79:8080/hpd-ws/ProviderInformationDirectoryService") {
			@Override
			public void doWithMessage(final WebServiceMessage message)
					throws IOException {
				try {
					SaajSoapMessage saajSoapMessage = (SaajSoapMessage) message;
					saajSoapMessage.setSoapAction(
							"urn:ihe:iti:2010:ProviderInformationQuery");
					saajSoapMessage.getEnvelope().addNamespaceDeclaration(
							"urn", "urn:oasis:names:tc:DSML:2:0:core");

					SoapHeader springSoapHeader = saajSoapMessage
							.getSoapHeader();
					springSoapHeader.addNamespaceDeclaration("wsa",
							"http://www.w3.org/2005/08/addressing");

					SOAPMessage soapMessage = saajSoapMessage.getSaajMessage();
					SOAPPart soapPart = soapMessage.getSOAPPart();

					SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

					SOAPHeader soapHeader = soapEnvelope.getHeader();

					Name headerElementName = soapEnvelope.createName(
							"Security", WSSecurityConstants.WSSE_PREFIX,
							WSSecurityConstants.WSSE_NS);

					// Add "Security" soapHeaderElement to soapHeader
					SOAPHeaderElement soapHeaderElement = soapHeader
							.addHeaderElement(headerElementName);

					soapHeaderElement.addNamespaceDeclaration(
							WSSecurityConstants.WSU_PREFIX,
							WSSecurityConstants.WSU_NS);

					// Add usernameToken to "Security" soapHeaderElement
					SOAPElement usernameTokenSOAPElement = soapHeaderElement
							.addChildElement("UsernameToken",
									WSSecurityConstants.WSSE_PREFIX,
									WSSecurityConstants.WSSE_NS);

					// Add username to usernameToken
					SOAPElement userNameSOAPElement = usernameTokenSOAPElement
							.addChildElement("Username",
									WSSecurityConstants.WSSE_PREFIX,
									WSSecurityConstants.WSSE_NS);
					userNameSOAPElement.addTextNode(username);

					// Add password to usernameToken
					SOAPElement passwordSOAPElement = usernameTokenSOAPElement
							.addChildElement("Password",
									WSSecurityConstants.WSSE_PREFIX,
									WSSecurityConstants.WSSE_NS);

					passwordSOAPElement.addTextNode(password);

					SoapHeaderElement wsaAction = springSoapHeader
							.addHeaderElement(new QName(
									"http://www.w3.org/2005/08/addressing",
									"Action", "wsa"));
					wsaAction.setText(
							"urn:ihe:iti:2010:ProviderInformationQuery");

					SoapHeaderElement wsaMessageId = springSoapHeader
							.addHeaderElement(new QName(
									"http://www.w3.org/2005/08/addressing",
									"MessageID", "wsa"));
					wsaMessageId
							.setText("uuid:" + UUID.randomUUID().toString());
					System.out
							.println("\n------------ Batch Request with Header"
									+ " ------------");
					// saajSoapMessage.writeTo(System.out);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					saajSoapMessage.writeTo(bos);
					ByteArrayInputStream bis = new ByteArrayInputStream(
							bos.toByteArray());
					Document doc = DocumentBuilderFactory.newInstance()
							.newDocumentBuilder().parse(bis);
					printDocument(doc, System.out);
					System.out.println();
				} catch (SOAPException | TransformerException | SAXException
						| ParserConfigurationException soapException) {
					throw new RuntimeException("WSSESecurityHeaderRequest"
							+ "WebServiceMessageCallback", soapException);
				}

			}
		};

		@SuppressWarnings("unchecked")
		JAXBElement<BatchResponse> jaxbBatchResponse =
		    (JAXBElement<BatchResponse>) getWebServiceTemplate()
				.marshalSendAndReceive(jaxbBatchRequest, soapActionCallback);

		BatchResponse response = (BatchResponse) jaxbBatchResponse.getValue();

		return response;
	}

	/**
	 * Pretty prints the given Document to the given OutputStream.
	 *
	 * @param doc
	 *            the Document to pretty print
	 * @param out
	 *            the OutputSteam for printing
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws TransformerException
	 *             the transformer exception
	 */
	public static void printDocument(final Document doc, final OutputStream out)
			throws IOException, TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl",null);
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(doc), new StreamResult(
				new OutputStreamWriter(out, "UTF-8")));
	}


	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 * @throws Exception when username is null or empty
	 */
	public void setUsername(String username) throws Exception {
		if(username == null || username.isEmpty()) {
			throw new Exception("Username must not be 'null' or empty");
		}

		this.username = username;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the password to set
	 * @throws Exception when password is NULL or empty.
	 */
	public void setPassword(String password) throws Exception {
		if(password == null || password.isEmpty()) {
			throw new Exception("Password must not be 'null' or empty");
		}
		this.password = password;
	}
}
