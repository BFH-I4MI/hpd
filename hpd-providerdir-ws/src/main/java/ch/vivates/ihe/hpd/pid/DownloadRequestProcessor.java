package ch.vivates.ihe.hpd.pid;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.w3c.dom.Document;

public class DownloadRequestProcessor {

	private int fromMonthLimit = 12;

	public Map<String, Object> extractParams(
			@XPath(value = "/cs:downloadRequest/@requestID", namespaces = @NamespacePrefix(prefix = "cs", uri = "urn:ehealth-suisse:names:tc:CS:1"), resultType = String.class) String requestID,
			@XPath(value = "/cs:downloadRequest/@fromDate", namespaces = @NamespacePrefix(prefix = "cs", uri = "urn:ehealth-suisse:names:tc:CS:1"), resultType = String.class) String fromDateStr,
			@XPath(value = "/cs:downloadRequest/@toDate", namespaces = @NamespacePrefix(prefix = "cs", uri = "urn:ehealth-suisse:names:tc:CS:1"), resultType = String.class) String toDateStr,
			@XPath(value = "/cs:downloadRequest/@filterMyTransactions", namespaces = @NamespacePrefix(prefix = "cs", uri = "urn:ehealth-suisse:names:tc:CS:1"), resultType = String.class) String filterMyTransactions, @Header("principal") String principal) throws ParseException {
		Map<String, Object> requestParamsMap = new HashMap<String, Object>();
		requestParamsMap.put("request_id", requestID);
		DateTime limitDateTime = DateTime.now().minusMonths(fromMonthLimit);
		DateTime fromDate = ISODateTimeFormat.dateTimeParser().parseDateTime(fromDateStr);
		requestParamsMap.put("from_date", limitDateTime.isBefore(fromDate) ? fromDate.toString() : limitDateTime.toString());
		if (toDateStr == null || toDateStr.isEmpty()) {
			requestParamsMap.put("to_date",  DateTime.now().toString());
		} else {
			requestParamsMap.put("to_date", ISODateTimeFormat.dateTimeParser().parseDateTime(toDateStr).toString());
		}
		requestParamsMap.put("filtered_user", Boolean.valueOf(filterMyTransactions) ? principal : "#####");
		return requestParamsMap;
	}

	public byte[] processResponse(@Body List<Map<String, Object>> results) throws Exception {
		StringBuilder sb = new StringBuilder(results.size() * 2000);

		for (Map<String, Object> c : results) {
			sb.append(c.get("tr_body"));
		}
		String response = "<cs:downloadResponse xmlns:cs=\"urn:ehealth-suisse:names:tc:CS:1\">" + sb.toString() + "</cs:downloadResponse>";

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document doc = builder.parse(new ByteArrayInputStream(response.getBytes()));
		doc.setXmlVersion("1.0");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Source domSource = new DOMSource(doc);
		StreamResult result = new StreamResult(out);
		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty("omit-xml-declaration", "yes");
		tr.transform(domSource, result);
		return out.toByteArray();

	}

	public void setFromMonthLimit(int fromMonthLimit) {
		this.fromMonthLimit = fromMonthLimit;
	}

	public static Date max(Date d1, Date d2) {
		if (d1 == null && d2 == null)
			return null;
		if (d1 == null)
			return d2;
		if (d2 == null)
			return d1;
		return (d1.after(d2)) ? d1 : d2;
	}

}
