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

/**
 * The Class JaxpUtil contains information output methods used for JAXB implementation.
 * 
 * @author Federico Marmory, Post CH, major development
 * @author Kevin Tippenhauer, Berner Fachhochschule, javadoc
 */
public class JaxpUtil {
	
	/** The Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(JaxpUtil.class);
	
	/**
	 * Prints the info.
	 *
	 * @throws DatatypeConfigurationException the data type configuration exception
	 * @throws SAXException the SAX exception
	 */
	public void printInfo() throws DatatypeConfigurationException, SAXException {
		OutputJaxpImplementationInfo();
	}
		
	/**
	 * Output JAXP implementation information.
	 *
	 * @throws DatatypeConfigurationException the data type configuration exception
	 * @throws SAXException the SAX exception
	 */
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

	/**
	 * Gets the JAXP implementation information.
	 *
	 * @param <T> the generic type
	 * @param componentName the component name
	 * @param componentClass the component class
	 * @return the JAXP implementation information
	 */
	private static <T> String getJaxpImplementationInfo(String componentName, Class<T> componentClass) {
	    CodeSource source = componentClass.getProtectionDomain().getCodeSource();
	    return MessageFormat.format(
	            "{0} implementation: {1} loaded from: {2}",
	            componentName,
	            componentClass.getName(),
	            source == null ? "Java Runtime" : source.getLocation());
	}
	
}
