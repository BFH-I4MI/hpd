package ch.bfh.i4mi.client;

import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.transform.stream.StreamResult;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import ch.vivates.ihe.hpd.pid.model.cs.AttributeDescription;
import ch.vivates.ihe.hpd.pid.model.cs.BatchRequest;
import ch.vivates.ihe.hpd.pid.model.cs.BatchResponse;
import ch.vivates.ihe.hpd.pid.model.cs.Filter;
import ch.vivates.ihe.hpd.pid.model.cs.ObjectFactory;
import ch.vivates.ihe.hpd.pid.model.cs.SearchRequest;

/**
 * The Class Application.
 */
public final class Application {

	/**
	 * Instantiates a new application.
	 */
	private Application() {
		// This constructor is intentionally empty. Nothing special is needed here.
	}

	/**
	 * The main method builds the BatchRequest, sends the request to the web
	 * service and receives the response.
	 * 
	 *
	 * @param args
	 *            the arguments for the application.
	 *            
	 * @throws SOAPException the SOAPException
	 */
	public static void main(final String... args) throws SOAPException {

		final ApplicationContext ctx = SpringApplication.run(
				ClientConfiguration.class, args);

		final HPDClient hpdClient = ctx.getBean(HPDClient.class);

		final AttributeDescription attrDesc = new AttributeDescription();
		attrDesc.setName("objectClass");

		final Filter filter = new Filter();
		filter.setPresent(attrDesc);

		final SearchRequest searchRequest = new ObjectFactory()
				.createSearchRequest();
		searchRequest.setDn("ou=HCProfessional,dc=HPD,o=ehealth-suisse,c=ch");
		searchRequest.setRequestID("01");
		searchRequest.setScope("wholeSubtree");
		searchRequest.setDerefAliases("neverDerefAliases");
		searchRequest.setSizeLimit(0L);
		searchRequest.setTimeLimit(0L);
		searchRequest.setTypesOnly(false);
		searchRequest.setFilter(filter);

		final BatchRequest batchRequest = new BatchRequest();

		batchRequest.setRequestID("0001");
		batchRequest.setProcessing("sequential");
		batchRequest.setResponseOrder("sequential");
		batchRequest.setOnError("exit");
		batchRequest.getBatchRequests().add(searchRequest);

		final ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
		map.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);

		final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan("ch.vivates.ihe.hpd.pid.model.cs");
		marshaller.setSupportJaxbElementClass(true);
		marshaller.setMarshallerProperties(map);
		marshaller.setUnmarshallerProperties(map);

		final BatchResponse batchResponse = hpdClient
				.getBatchResponse(batchRequest);

		if (batchResponse != null) {
			System.out.println("\n------------ Batch Response ------------");

			// Wrapping in JAXBElement is needed because of missing
			// XMLRootElement tags.
			marshaller.marshal(new JAXBElement<BatchResponse>(new QName(
					"urn:BatchResponse"), BatchResponse.class, batchResponse),
					new StreamResult(System.out));

		}

	}
}
