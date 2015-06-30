package ch.vivates.ihe.hpd.pid;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.camel.language.NamespacePrefix;
import org.apache.camel.language.XPath;
import org.apache.directory.api.dsmlv2.ParserUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ch.vivates.tools.dsmlv2.Dsmlv2Engine;


public class BatchRequestProcessor {
	
	private Dsmlv2Engine dsmlEngine;
	
	public byte[] processQueryRequest(@Body InputStream dsmlQuery) throws Exception  {
		ByteArrayOutputStream response = new ByteArrayOutputStream();
		dsmlEngine.processDSML(dsmlQuery, response);
		return  response.toByteArray();
	}
	
	public InputStream prepareFeedRequest(@Body InputStream dsmlFeed, @Header("requestID") String requestID, @Header("processing") String processing,
			@Header("responseOrder") String responseOrder, @Header("onError") String onError) throws Exception {
		return injectBachRequestNode(dsmlFeed, requestID, processing, responseOrder, onError);
	}
	
	public byte[] processFeedRequest(@Body InputStream dsmlFeed) throws Exception  {
		ByteArrayOutputStream response = new ByteArrayOutputStream();
		dsmlEngine.processDSML(dsmlFeed, response);
		return  response.toByteArray();
	}

	public Map<String, Object> processResponse(
			@XPath(value="/dsml:batchResponse//dsml:resultCode/@code", 
					namespaces=@NamespacePrefix(prefix="dsml", uri="urn:oasis:names:tc:DSML:2:0:core") , resultType=String.class) String resultCode,
			@XPath(value="/dsml:batchResponse//dsml:errorMessage/text()", 
					namespaces=@NamespacePrefix(prefix="dsml", uri="urn:oasis:names:tc:DSML:2:0:core") , resultType=String.class) String errorMessage, 
			@Body String responseBody) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("resultCode", resultCode);
		responseMap.put("errorMessage", errorMessage);
		responseMap.put("responseBody", responseBody);
		return responseMap;
	}

	public void setDsmlEngine(Dsmlv2Engine dsmlEngine) {
		this.dsmlEngine = dsmlEngine;
	}
	
	private InputStream injectBachRequestNode(InputStream is, String requestID, String processing, String responseOrder, String onError) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document oldDoc = builder.parse(is);
        Node oldRoot = oldDoc.getDocumentElement();
        Document newDoc = builder.newDocument();
        Element newRoot = newDoc.createElementNS(ParserUtils.DSML_NAMESPACE.getURI(), "dsml:batchRequest");
        if(requestID != null && !requestID.isEmpty()) {
        	newRoot.setAttribute("requestID", requestID);
        }
        if(processing != null && !processing.isEmpty()) {
        	newRoot.setAttribute("processing", processing);
        }
        if(responseOrder != null && !responseOrder.isEmpty()) {
           	newRoot.setAttribute("responseOrder", responseOrder);
        }
        if(onError != null && !onError.isEmpty()) {
        	newRoot.setAttribute("onError", onError);
        }
        newDoc.appendChild(newRoot);
        newRoot.appendChild(newDoc.importNode(oldRoot, true));
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Source domSource = new DOMSource(newDoc);
        StreamResult result = new StreamResult(out);
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty("omit-xml-declaration", "yes");
        tr.transform(domSource, result);
        return new ByteArrayInputStream(out.toByteArray());
    }
	
}
