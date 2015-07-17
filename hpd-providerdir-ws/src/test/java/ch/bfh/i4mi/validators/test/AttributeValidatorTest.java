package ch.bfh.i4mi.validators.test;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.soap.SOAPException;

import org.junit.Before;
import org.junit.Test;

import types.termserver.fhdo.de.CodeSystemConcept;
import ch.bfh.i4mi.validators.AttributeValidator;
import ch.bfh.i4mi.validators.AttributeValidatorSettings;

public class AttributeValidatorTest {
	
	AttributeValidatorSettings avs;
	AttributeValidator av;

	@Before
	public void setUp() throws FileNotFoundException {
		avs = new AttributeValidatorSettings(new BufferedInputStream(new FileInputStream(
				"AttributeValidator/AttributeValidator.properties")));
		av = new AttributeValidator(avs);
	}

	@Test
	public void testGetCurrentCodeSystemVersion() throws SOAPException {
		// Positive test
		assertEquals(av.getCurrentCodeSystemVersion("authorSpecialty"), 6l);
		
		// Negative test
		assertEquals(av.getCurrentCodeSystemVersion("author_Specialty"), -1l);
	}

	@Test
	public void testListCodeSystemConcepts() throws SOAPException {
		// Positive test
		CodeSystemConcept csc = av.currentConceptCodeFilter("healthcareFacilityTypeCode", "190001");
		assertEquals(csc.getTerm(), "Institut für medizinische Diagnostik");
		assertEquals(csc.getCode(), "190001");
		
		// Negative test
		csc = av.currentConceptCodeFilter("healthcareFacilityTypeCode", "000000");
		assertNull(csc);
	}
	
	@Test
	public void testCheckTerminology() throws SOAPException {
		assertTrue(av.checkTerminology("HcSpecialisation", "260049"));
		assertTrue(av.checkTerminology("businessCategory", "190005"));
		assertTrue(av.checkTerminology("asdf", "asdf")); // Not in properties file
		assertTrue(av.checkTerminology("hcIdentifier", "asdf"));  // In properties file with NoCodeSystem
		assertFalse(av.checkTerminology("HcSpecialisation", "190005"));
		assertFalse(av.checkTerminology("HcSpecialisation", "26"));
	}
}
