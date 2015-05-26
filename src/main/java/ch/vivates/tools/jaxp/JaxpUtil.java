package ch.vivates.tools.jaxp;

import java.security.CodeSource;
import java.text.MessageFormat;

import javax.xml.XMLConstants;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

public class JaxpUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(JaxpUtil.class);
	
	public void printInfo() throws DatatypeConfigurationException, SAXException {
		OutputJaxpImplementationInfo();
	}
		
	private static void OutputJaxpImplementationInfo() throws DatatypeConfigurationException, SAXException {
		LOG.debug(getJaxpImplementationInfo("DocumentBuilderFactory", DocumentBuilderFactory.newInstance().getClass()));
		LOG.debug(getJaxpImplementationInfo("XPathFactory", XPathFactory.newInstance().getClass()));
		LOG.debug(getJaxpImplementationInfo("TransformerFactory", TransformerFactory.newInstance().getClass()));
		LOG.debug(getJaxpImplementationInfo("SAXParserFactory", SAXParserFactory.newInstance().getClass()));
		LOG.debug(getJaxpImplementationInfo("XMLInputFactory", XMLInputFactory.newInstance().getClass()));
		LOG.debug(getJaxpImplementationInfo("XMLOutputFactory", XMLOutputFactory.newInstance().getClass()));
		LOG.debug(getJaxpImplementationInfo("DatatypeFactory", DatatypeFactory.newInstance().getClass()));
		LOG.debug(getJaxpImplementationInfo("XMLEventFactory", XMLEventFactory.newInstance().getClass()));
		LOG.debug(getJaxpImplementationInfo("SchemaFactory", SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).getClass()));
		LOG.debug(getJaxpImplementationInfo("XMLReaderFactory", XMLReaderFactory.createXMLReader().getClass()));
	}

	private static <T> String getJaxpImplementationInfo(String componentName, Class<T> componentClass) {
	    CodeSource source = componentClass.getProtectionDomain().getCodeSource();
	    return MessageFormat.format(
	            "{0} implementation: {1} loaded from: {2}",
	            componentName,
	            componentClass.getName(),
	            source == null ? "Java Runtime" : source.getLocation());
	}
	
}
