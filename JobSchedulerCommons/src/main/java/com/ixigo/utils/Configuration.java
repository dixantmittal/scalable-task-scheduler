package com.ixigo.utils;

import com.ixigo.cache.CacheManager;
import com.ixigo.cache.impl.ConfigCache;
import com.ixigo.constants.CommonConstants;
import com.ixigo.constants.IConfigurationConstants;
import com.ixigo.exception.ServiceException;
import com.ixigo.exception.codes.CommonExceptionCodes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dixant on 04/04/17.
 */
public class Configuration {
    public static Map<String, String> getConfigMap(String configType) {
        ConfigCache configCache = CacheManager.getInstance().getCache(ConfigCache.class);
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
