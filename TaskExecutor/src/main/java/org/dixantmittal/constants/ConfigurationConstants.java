package org.dixantmittal.constants;

/**
 * Created by dixant on 29/03/17.
 */
public enum ConfigurationConstants implements IConfigurationConstants {
    TASK_MANAGER_SERVER_IP("taskmanager.server.ip", "127.0.0.1"),
    TASK_MANAGER_SERVER_PORT("taskmanager.server.port", "8080");

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
