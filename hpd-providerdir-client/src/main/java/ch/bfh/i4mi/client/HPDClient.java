package ch.bfh.i4mi.client;

import java.io.IOException;
import java.util.UUID;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
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

import org.opensaml.ws.wssecurity.WSSecurityConstants;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import ch.vivates.ihe.hpd.pid.model.cs.BatchRequest;
import ch.vivates.ihe.hpd.pid.model.cs.BatchResponse;


public class HPDClient extends WebServiceGatewaySupport {

	public BatchResponse getBatchResponse(BatchRequest batchRequest)
			throws SOAPException {

		/*
		 * Credentials credentials = new UsernamePasswordCredentials("com_test",
		 * "com"); HttpComponentsMessageSender sender = new
		 * HttpComponentsMessageSender(); sender.setCredentials(credentials);
		 * getWebServiceTemplate().setMessageSender(sender);
		 */
		SaajSoapMessageFactory saajSoapMessageFactory = new SaajSoapMessageFactory();
		saajSoapMessageFactory.setSoapVersion(SoapVersion.SOAP_12);
		MessageFactory mf12 = MessageFactory
				.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		saajSoapMessageFactory.setMessageFactory(mf12);

		getWebServiceTemplate().setMessageFactory(saajSoapMessageFactory);
		/*
		 * System.out.println(getWebServiceTemplate().getDefaultUri().toString())
		 * ;
		 * 
		 * System.out.print("Stream Output: ");
		 * 
		 * Result result = new StreamResult(System.out); System.out.println();
		 * 
		 * Marshaller marshaller = getWebServiceTemplate().getMarshaller(); try
		 * { marshaller.marshal(new JAXBElement<BatchRequest>( new
		 * QName("urn:batchRequest"), BatchRequest.class, batchRequest),
		 * result); } catch (XmlMappingException | IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		JAXBElement<BatchRequest> jaxbBatchRequest = new JAXBElement<BatchRequest>(
				new QName("urn:batchRequest"), BatchRequest.class, batchRequest);

		SoapActionCallback soapActionCallback = new SoapActionCallback(
				"http://epdhpd.i4mi.bfh.ch:8080/hpd-ws/ProviderInformationDirectoryService") {
			@Override
			public void doWithMessage(WebServiceMessage message)
					throws IOException {
				try {
					SaajSoapMessage saajSoapMessage = (SaajSoapMessage) message;
					saajSoapMessage
							.setSoapAction("urn:ihe:iti:2010:ProviderInformationQuery");
					saajSoapMessage.getEnvelope().addNamespaceDeclaration(
							"urn", "urn:oasis:names:tc:DSML:2:0:core");

					SoapHeader springSoapHeader = saajSoapMessage
							.getSoapHeader();
					springSoapHeader.addNamespaceDeclaration("wsa",
							"http://www.w3.org/2005/08/addressing");

					/*
					 * System.out.println(soapHeader.getAllAttributes().next().
					 * toString());
					 * System.out.println(soapMessage.getSoapHeader(
					 * ).getAllAttributes().next().toString());
					 * SoapHeaderElement wsseSecurity =
					 * soapHeader.addHeaderElement(new QName("Security",
					 * "wsse")); wsseSecurity.setText(
					 * "urn:ihe:iti:2010:ProviderInformationQuery");
					 */

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

					// usernameTokenSOAPElement.addAttribute(new
					// QName("wsu:Id"),
					// "UsernameToken-EB07CFD77F31B506F2143558113620534");

					// Add username to usernameToken
					SOAPElement userNameSOAPElement = usernameTokenSOAPElement
							.addChildElement("Username",
									WSSecurityConstants.WSSE_PREFIX,
									WSSecurityConstants.WSSE_NS);
					userNameSOAPElement.addTextNode("com_be");

					// Add password to usernameToken
					SOAPElement passwordSOAPElement = usernameTokenSOAPElement
							.addChildElement("Password",
									WSSecurityConstants.WSSE_PREFIX,
									WSSecurityConstants.WSSE_NS);

					// passwordSOAPElement.addAttribute(new QName("Type"),
					// "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
					passwordSOAPElement.addTextNode("com");

					// SOAPElement
					// nonceSOAPElement =
					// usernameTokenSOAPElement.addChildElement("Nonce",
					// WSSecurityConstants.WSSE_PREFIX,
					// WSSecurityConstants.WSSE_NS);
					// nonceSOAPElement.addAttribute(new QName("EncodingType"),
					// "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
					// nonceSOAPElement.addTextNode("rX6z0Q4Fle0CK+yoQf7VHg==");
					//
					// SOAPElement
					// createdSOAPElement =
					// usernameTokenSOAPElement.addChildElement("Created",
					// WSSecurityConstants.WSU_PREFIX,
					// WSSecurityConstants.WSU_NS);
					//
					// createdSOAPElement.addTextNode("2015-06-29T13:40:38.931Z");

					SoapHeaderElement wsaAction = springSoapHeader
							.addHeaderElement(new QName(
									"http://www.w3.org/2005/08/addressing",
									"Action", "wsa"));
					wsaAction
							.setText("urn:ihe:iti:2010:ProviderInformationQuery");

					SoapHeaderElement wsaMessageId = springSoapHeader
							.addHeaderElement(new QName(
									"http://www.w3.org/2005/08/addressing",
									"MessageID", "wsa"));
					wsaMessageId
							.setText("uuid:" + UUID.randomUUID().toString());
					System.out.println();
					saajSoapMessage.writeTo(System.out);
					System.out.println();
				} catch (SOAPException soapException) {
					throw new RuntimeException(
							"WSSESecurityHeaderRequestWebServiceMessageCallback",
							soapException);
				}

			}
		};

		@SuppressWarnings("unchecked")
		JAXBElement<BatchResponse> jaxbBatchResponse = (JAXBElement<BatchResponse>) getWebServiceTemplate()
				.marshalSendAndReceive(
						jaxbBatchRequest, soapActionCallback);
		BatchResponse response = (BatchResponse) jaxbBatchResponse.getValue();

		return response;
	}
}
