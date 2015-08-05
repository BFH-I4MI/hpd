package ch.bfh.i4mi.validators.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

import ch.bfh.i4mi.validators.AttributeValidatorSettings;

/**
 * The Class AttributeValidatorSettingsTest.
 * 
 * @author Kevin Tippenhauer, Berner Fachhochschule, javadoc
 */
public class AttributeValidatorSettingsTest {

	/** The attribute validator settings. */
	AttributeValidatorSettings avs;
	
	/**
	 * Test to create a new AttributeValidatorSettings object.
	 *
	 * @throws FileNotFoundException the file not found exception
	 */
	@Test
	public void testAttributeValidatorSettings() throws FileNotFoundException {
		avs = new AttributeValidatorSettings(new BufferedInputStream(new FileInputStream(
				"AttributeValidator/AttributeValidator.properties")));
	}
	
	/**
	 * Test to get translation method.
	 *
	 * @throws FileNotFoundException the file not found exception
	 */
	@Test
	public void testGetTranslation() throws FileNotFoundException {
		avs = new AttributeValidatorSettings(new BufferedInputStream(new FileInputStream(
				"AttributeValidator/AttributeValidator.properties")));
		assertEquals(avs.getTranslation("HcSpecialisation"),
				"practiceSettingCode");
		assertEquals(avs.getTranslation("businessCategory"),
				"healthcareFacilityTypeCode");
	}
}
