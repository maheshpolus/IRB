package org.mit.irb.web.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class get details from property files and load data to class 
 * @author anumole
 *
 */
public class IRBProperties{
	
	private static Properties props = null;
	/*private static final String COMPANION_PROP_FILE="/mit-irb.properties";*/
	private static final String COMPANION_PROP_FILE="/mit-irb.properties";
	
	/**
	 * Constructor Creates a new instance of IRBProperties
	 */
	private IRBProperties() {}
	
	/**
	 * Loads properties from the mapperd budget companion property file
	 * @return
	 * @throws IOException
	 */
	private static Properties loadProperties() throws IOException {

		InputStream stream = null;
		try {
			props = new Properties();
			stream = new IRBProperties().getClass().getResourceAsStream(COMPANION_PROP_FILE);
			props.load( stream );
		} finally {
			try {
				stream.close();
			} catch (Exception ex) {
			}
		}
		return props;
	}
	
	/**
	 * retrieves single property value from property file - mit-irb.properties
	 * @param key
	 * @param defaultValue
	 * @return The property value or default value if no property exists
	 * @throws IOException
	 */
	public static String getProperty(String key,String defaultValue) throws IOException {
		if (props == null) {
			synchronized (IRBProperties.class) {
				if (props == null) {
					props = loadProperties();
				}
			}
		}
		return props.getProperty(key,defaultValue);
	}
	/**
	 * Get a property value from the <code>mit-irb.properties</code> file.
	 * @param key
	 * @return The property value or default value if no property exists
	 * @throws IOException
	 */
	public static String getProperty(String key) throws IOException {
		return getProperty(key,null);
	}
}