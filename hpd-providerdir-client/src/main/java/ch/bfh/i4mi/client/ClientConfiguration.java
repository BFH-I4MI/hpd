package ch.bfh.i4mi.client;

import java.util.HashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

// TODO: Auto-generated Javadoc
/**
 * The Class ClientConfiguration contains the configuration for the HPDClient
 * class.
 */
@Configuration
public class ClientConfiguration {
	
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
	public final Jaxb2Marshaller marshaller() {

		final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setCheckForXmlRootElement(false);
		final HashMap<String, Boolean> map = new HashMap<String, Boolean>();
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
	 */
	@Bean
	public final HPDClient hpdClient(final Jaxb2Marshaller marshaller) {
		final HPDClient client = new HPDClient();
		client.setDefaultUri("http://147.87.117.79:8080/hpd-ws/"
				+ "ProviderInformationDirectoryService");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}
