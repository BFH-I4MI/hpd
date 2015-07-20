package ch.vivates.ihe.hpd.pid;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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
import org.apache.directory.api.dsmlv2.ParserUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The Class TransactionsHistoryItemExtractor.
 * 
 * @author Federico Marmory, Post CH, major development
 * @author Kevin Tippenhauer, Berner Fachhochschule, javadoc
 */
public class TransactionsHistoryItemExtractor {
	
	/** The Constant SDF containing the SimpleDateFormat. */
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	/**
	 * Converts the transactions items into a map.
	 *
	 * @param trDate the transaction date
	 * @param trID the transaction id
	 * @param trContentStream the transaction content stream
	 * @param principal the principal
	 * @return the map with the items
	 * @throws Exception the exception
	 */
	public Map<String, Object> convert(@Header("HpdPidInDate") String trDate, @Header("breadcrumbId") String trID,
			@Body InputStream trContentStream, @Header("principal") String principal) throws Exception {
		Map<String, Object> items = new HashMap<String, Object>();
		items.put("tr_op_idx", trID);
		items.put("src_desc", principal);
		items.put("tr_date", SDF.parse(trDate));
		items.put("tr_body", injectAuthRequestNode(trContentStream, principal));
		return items;
	}
	
	/**
	 * Inject auth request node.
	 *
	 * @param is the input stream
	 * @param principal the principal
	 * @return the input stream with the injected authentication request node
	 * @throws Exception the exception
	 */
	private InputStream injectAuthRequestNode(InputStream is, String principal) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(is);
        Node docRoot = doc.getDocumentElement();
        Element authNode = doc.createElementNS(ParserUtils.DSML_NAMESPACE.getURI(), "dsml:authRequest");
       	authNode.setAttribute("principal", principal);
        docRoot.appendChild(authNode);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Source domSource = new DOMSource(doc);
        StreamResult result = new StreamResult(out);
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty("omit-xml-declaration", "yes");
        tr.transform(domSource, result);
        return new ByteArrayInputStream(out.toByteArray());
    }
	
}
