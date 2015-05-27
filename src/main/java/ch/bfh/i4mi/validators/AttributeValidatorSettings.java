package ch.bfh.i4mi.validators;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AttributeValidatorSettings {
	
	private BufferedInputStream bufferedInputStream;
	private Map<String, String> translationDictionary = new HashMap<String, String>();

	public AttributeValidatorSettings(BufferedInputStream aBufferedInputStream) {
		this.bufferedInputStream = aBufferedInputStream;
		try {
			loadSettings();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage() + ":" + e.getStackTrace());
		}
	}
	
	private void loadSettings() throws IOException {
		Properties properties = new Properties();
		properties.load(bufferedInputStream);
		bufferedInputStream.close();
		for(String key : properties.stringPropertyNames()) {
			translationDictionary.put(key, properties.getProperty(key));
		}
	}
	
	public String getTranslation(String key) {
		String result = translationDictionary.get(key);
		return result;
	}
	

}
