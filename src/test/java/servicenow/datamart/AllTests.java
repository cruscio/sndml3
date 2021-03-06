package servicenow.datamart;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import servicenow.core.*;
import servicenow.datamart.Database;


@RunWith(Suite.class)
@SuiteClasses({ 
	DateTimeTest.class, 
	InstanceTest.class, 
	ParametersTest.class })

public class AllTests {

	public static final Logger logger = LoggerFactory.getLogger(AllTests.class);
	public static final Marker mrkTest = MarkerFactory.getMarker("TEST");
	
	static Database dbw = null;
	static Properties properties = null;
		
	public static Database getDBWriter() throws IOException, java.sql.SQLException {
		try {
			dbw = new Database(TestingManager.getProperties());
		} catch (URISyntaxException e) {
			throw new TestingException(e);
		}
		return dbw;
	}

	@SuppressWarnings("rawtypes")
	public static Logger getLogger(Class cls) {
		return servicenow.core.TestingManager.getLogger(cls);
	}

	
}
