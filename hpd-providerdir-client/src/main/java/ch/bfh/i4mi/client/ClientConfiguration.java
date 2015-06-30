package ch.bfh.i4mi.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class ClientConfiguration {
	@Bean
	public Jaxb2Marshaller marshaller() {
		
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setCheckForXmlRootElement(false);
		
		marshaller.setContextPath("ch.vivates.ihe.hpd.pid.model.cs");
		return marshaller;
	}

	@Bean
	public HPDClient hpdClient(Jaxb2Marshaller marshaller) {
		HPDClient client = new HPDClient();
		client.setDefaultUri("http://epdhpd.i4mi.bfh.ch:8080/hpd-ws/ProviderInformationDirectoryService");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}
