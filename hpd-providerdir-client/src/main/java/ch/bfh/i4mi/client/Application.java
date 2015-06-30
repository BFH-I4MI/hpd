package ch.bfh.i4mi.client;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import ch.vivates.ihe.hpd.pid.model.cs.AttributeDescription;
import ch.vivates.ihe.hpd.pid.model.cs.BatchRequest;
import ch.vivates.ihe.hpd.pid.model.cs.BatchResponse;
import ch.vivates.ihe.hpd.pid.model.cs.Filter;
import ch.vivates.ihe.hpd.pid.model.cs.ObjectFactory;
import ch.vivates.ihe.hpd.pid.model.cs.SearchRequest;

public class Application {

	public static void main(String[] args) {
		
		ApplicationContext ctx = SpringApplication.run(ClientConfiguration.class, args);

		HPDClient hpdClient = ctx.getBean(HPDClient.class);

		AttributeDescription attributeDescription = new AttributeDescription();
		attributeDescription.setName("objectClass");

		Filter filter = new Filter();
		filter.setPresent(attributeDescription);
		
		SearchRequest searchRequest = new ObjectFactory().createSearchRequest();
		searchRequest.setDn("ou=HCProfessional,dc=HPD,o=ehealth-suisse,c=ch");
		searchRequest.setRequestID("01");
		searchRequest.setScope("wholeSubtree");
		searchRequest.setDerefAliases("neverDerefAliases");
		searchRequest.setSizeLimit(0L);
		searchRequest.setTimeLimit(0L);
		searchRequest.setTypesOnly(false);
		searchRequest.setFilter(filter);
		
		
		BatchRequest batchRequest = new BatchRequest();
		
		batchRequest.setRequestID("0001");
		batchRequest.setProcessing("sequential");
		batchRequest.setResponseOrder("sequential");
		batchRequest.setOnError("exit");
		batchRequest.getBatchRequests().add(searchRequest);
		
		BatchResponse batchResponse = null;
		try {
			batchResponse = hpdClient.getBatchResponse(batchRequest);
			System.out.println(batchResponse);
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(batchResponse != null) {
			Result result = new StreamResult(System.out);
			System.out.println();

			Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
			marshaller.setPackagesToScan("ch.vivates.ihe.hpd.pid.model.cs");
			marshaller.setSupportJaxbElementClass(true);
//			marshaller.setCheckForXmlRootElement(false);
			
//			marshaller.setContextPath("ch.vivates.ihe.hpd.pid.model.cs");
			try {
				marshaller.marshal(new JAXBElement<BatchResponse>(new QName(
						"urn:BatchResponse"), BatchResponse.class, batchResponse),
						result);
				
				marshaller.marshal(batchResponse,
						result);
			} catch (XmlMappingException e) {
				e.printStackTrace();
			}
		}
		

	}

}
