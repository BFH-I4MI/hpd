package ch.bfh.i4mi.client;

import java.util.HashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * The Class ClientConfiguration contains the configuration for the HPDClient
 * class.
 */
@Configuration
public class ClientConfiguration {
	
	/** The Constant USERNAME provides the username used by the HPDClient. */
	private static final String USERNAME = "com_zh";
	
	/** The Constant PASSWORD provides the password used by the HPDClient.*/
	private static final String PASSWORD = "com";
	
	
	/**
	 * Instantiates a new client configuration.
	 */
	ClientConfiguration() {
		// This constructor is intentionally empty. Nothing special is needed here.
	}

	/**
	 * Returns the Marshaller of the HPDClient.
	 *
	 * @return the jaxb2 marshaller
	 */
	@Bean
	public Jaxb2Marshaller marshaller() {

		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setCheckForXmlRootElement(false);
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setMarshallerProperties(map);
		marshaller.setContextPath("ch.vivates.ihe.hpd.pid.model.cs");
		return marshaller;
	}

	/**
	 * Returns a new HPDClient instance with the delivered marshaller
	 * configured.
	 *
	 * @param marshaller
	 *            the marshaller used by the client
	 * @return the HPD client instance
	 * @throws Exception when HPDClient can not be created.
	 */
	@Bean
	public HPDClient hpdClient(final Jaxb2Marshaller marshaller) throws Exception {
		HPDClient client = new HPDClient(USERNAME, PASSWORD);
		client.setDefaultUri("http://epdhpd.i4mi.bfh.ch:8080/hpd-ws/"
				+ "ProviderInformationDirectoryService");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}
