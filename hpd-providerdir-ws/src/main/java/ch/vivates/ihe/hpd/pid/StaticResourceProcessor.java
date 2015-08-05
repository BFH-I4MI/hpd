package ch.vivates.ihe.hpd.pid;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

/**
 * The Class StaticResourceProcessor.
 * 
 * @author Federico Marmory, Post CH, major development
 * @author Kevin Tippenhauer, Berner Fachhochschule, javadoc
 */
public class StaticResourceProcessor {
	
	/** The location. */
	private Resource location; 
	
	/** The loaded resource. */
	private byte[] loadedResource;
	
	/**
	 * Processes the request.
	 *
	 * @return the byte array of the resource
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] processRequest() throws IOException {
		if(loadedResource == null) {
			loadedResource = IOUtils.toByteArray(location.getInputStream());
		}
		return loadedResource;
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(Resource location) {
		this.location = location;
	}
	
}
