package org.dixantmittal.utils;

import org.dixantmittal.cache.CacheManager;
import org.dixantmittal.cache.impl.ConfigurationCache;
import org.dixantmittal.constants.CommonConstants;
import org.dixantmittal.constants.IConfigurationConstants;
import org.dixantmittal.exception.ServiceException;
import org.dixantmittal.exception.codes.CommonExceptionCodes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dixant on 04/04/17.
 */
public class Configuration {
    public static Map<String, String> getConfigMap(String configType) {
        ConfigurationCache configCache = CacheManager.getInstance().getCache(ConfigurationCache.class);
        if (configCache == null) {
            throw new ServiceException(CommonExceptionCodes.CONFIG_CACHE_NOT_FOUND.code(),
                    CommonExceptionCodes.CONFIG_CACHE_NOT_FOUND.message());
        }
        return configCache.getOrDefault(configType, new HashMap<String, String>());
    }

    public static String getProperty(String configType, String configKey) {
        Map<String, String> configMap = getConfigMap(configType);
        return configMap.get(configKey);
    }

    public static String getGlobalProperty(IConfigurationConstants config) {
        String value = getProperty(CommonConstants.GLOBAL_PROPERTY, config.key());
        return (value != null) ? value : config.defaultValue();
    }
}
