package com.ixigo.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Properties;

public class SpringPropertiesUtil extends PropertyPlaceholderConfigurer {

	private static HashMap<String, String> systemPropertiesMap;

	private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;


	public static String getProperty(final String name) {
		return systemPropertiesMap.get(name);
	}

	@Override
	protected void processProperties(final ConfigurableListableBeanFactory beanFactory, final Properties props) throws BeansException {
		super.processProperties(beanFactory, props);
		systemPropertiesMap = new HashMap<String, String>();
		for (final Object key : props.keySet()) {
			final String keyStr = key.toString();
			final String valueStr = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
			systemPropertiesMap.put(keyStr, valueStr);
		}
	}

	@Override
	public void setSystemPropertiesMode(final int systemPropertiesMode) {
		super.setSystemPropertiesMode(systemPropertiesMode);
		springSystemPropertiesMode = systemPropertiesMode;
	}
}

