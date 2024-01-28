/*
 * EnvironmentUtil.java
 * 
 * Jeff Stone (jeffrey.l.stone@gmail.com)
 * 20210811
 * 20230819 - less hasDbInitializedEnvironment
 * 
 */
package org.jsquare.ss.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * @author jeffrey.l.stone
 *
 */
public class EnvironmentUtil {
	
	private static final String ALL_PROPERTIES_MSG = "-----All Properties---";
	private static final String PROPERTY_SOURCE_NAME_FORMAT = "--Property Source: {0}";
	private static final String PROPERTY_RECORD_FORMAT = "{0}={1}";
	private static final String PROPERTY_RECORD_OVERRIDE_FORMAT = "{0}={1} OVERRIDDEN to {2}";
	
	/**
	 * @param environment
	 * @return
	 */
	public static List<String> propertiesListing(ConfigurableEnvironment environment) {
		List<String> propertiesListing = new ArrayList<>();
        propertiesListing.add(ALL_PROPERTIES_MSG);
		for (PropertySource<?> propertySource : environment.getPropertySources()) {
			if (propertySource instanceof EnumerablePropertySource) {
				propertiesListing.add(MessageFormat.format(PROPERTY_SOURCE_NAME_FORMAT, propertySource.getName()));
				@SuppressWarnings("rawtypes")
				String[] propertyNames = ((EnumerablePropertySource) propertySource).getPropertyNames();
				for (String propertyName : propertyNames) {
					String resolvedProperty = environment.getProperty(propertyName);
					String sourceProperty = propertySource.getProperty(propertyName).toString();
					if (resolvedProperty.equals(sourceProperty)) {
						propertiesListing.add(MessageFormat.format(PROPERTY_RECORD_FORMAT, propertyName, resolvedProperty));
					} else {
						propertiesListing.add(MessageFormat.format(PROPERTY_RECORD_OVERRIDE_FORMAT, propertyName, sourceProperty, resolvedProperty));
					}
				}
			}
		}
		
		return propertiesListing;
	}

	/**
	 * @param environment
	 */
	public static void printProperties(ConfigurableEnvironment environment) {
		
		List<String> propertiesListing = propertiesListing(environment);

		for (String logRecord : propertiesListing) {
			log.info(logRecord);
		}
	}
	
	private EnvironmentUtil() {
		// Cannot instantiate this Library/Utility class!
	}
}
