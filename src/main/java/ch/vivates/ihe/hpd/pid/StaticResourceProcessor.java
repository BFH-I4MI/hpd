package ch.vivates.ihe.hpd.pid;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

public class StaticResourceProcessor {
	
	private Resource location; 
	
	private byte[] loadedResource;
	
	public byte[] processRequest() throws IOException {
		if(loadedResource == null) {
			loadedResource = IOUtils.toByteArray(location.getInputStream());
		}
		return loadedResource;
	}

	public void setLocation(Resource location) {
		this.location = location;
	}
	
}
