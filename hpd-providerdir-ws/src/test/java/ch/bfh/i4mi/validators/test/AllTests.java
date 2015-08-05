package ch.bfh.i4mi.validators.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * The Class AllTests runs all tests in the test suite.
 * 
 * @author Kevin Tippenhauer, Berner Fachhochschule
 */
@RunWith(Suite.class)
@SuiteClasses({ AttributeValidatorSettingsTest.class,
		AttributeValidatorTest.class })

public class AllTests {

}
