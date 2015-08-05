package ch.bfh.i4mi.validators;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * The Class AttributeValidatorSettings contains the settings for the AttributeValidator.
 * 
 * @author Kevin Tippenhauer, Berner Fachhochschule
 */
public class AttributeValidatorSettings {
	
	/** The buffered input stream. */
	private BufferedInputStream bufferedInputStream;
	
	/** The translation dictionary. */
	private Map<String, String> translationDictionary = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * Instantiates a new attribute validator settings object.
	 *
	 * @param aBufferedInputStream the a buffered input stream to load the settings
	 */
	public AttributeValidatorSettings(BufferedInputStream aBufferedInputStream) {
		this.bufferedInputStream = aBufferedInputStream;
		try {
			loadSettings();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage() + ":" + Arrays.toString(e.getStackTrace()));
		}
	}
	
	/**
	 * Loads the properties from the BufferedInputStream into the translation dictionary.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadSettings() throws IOException {
		Properties properties = new Properties();
		properties.load(bufferedInputStream);
		bufferedInputStream.close();
		for (String key : properties.stringPropertyNames()) {
			translationDictionary.put(key, properties.getProperty(key));
		}
	}
	
	/**
	 * Gets the translation for a given key.
	 *
	 * @param akey a key
	 * @return the translation for the key
	 */
	public String getTranslation(String akey) {
		String result = translationDictionary.get(akey);
		return result;
	}
	

}
