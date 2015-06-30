package ch.bfh.i4mi.validators.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

import ch.bfh.i4mi.validators.AttributeValidatorSettings;

public class AttributeValidatorSettingsTest {

	AttributeValidatorSettings avs;
	
	@Test
	public void testAttributeValidatorSettings() throws FileNotFoundException {
		avs = new AttributeValidatorSettings(new BufferedInputStream(new FileInputStream(
				"AttributeValidator/AttributeValidator.properties")));
	}
	
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
