package com.ixigo.constants;

import com.ixigo.constants.IConfigurationConstants;

/**
 * Created by dixant on 29/03/17.
 */
public enum ConfigurationConstants implements IConfigurationConstants {

    MAX_REFIRE_LIMIT("quartz.refire.limit.max", "5");

    private final String key;
    private final String defaultValue;

    ConfigurationConstants(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String defaultValue() {
        return defaultValue;
    }

    public String key() {
        return key;
    }
}
