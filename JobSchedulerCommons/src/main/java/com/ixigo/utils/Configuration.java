package com.ixigo.utils;

import com.ixigo.cache.CacheManager;
import com.ixigo.cache.impl.ConfigCache;
import com.ixigo.constants.CommonConstants;
import com.ixigo.constants.IConfigurationConstants;
import com.ixigo.exception.ServiceException;
import com.ixigo.exception.codes.CommonExceptionCodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public static Optional<String> getProperty(String configType, String configKey) {
        Map<String, String> configMap = getConfigMap(configType);
        return Optional.ofNullable(configMap.get(configKey));
    }
    
    public static String getGlobalProperty(IConfigurationConstants config) {
        Optional<String> value = getProperty(CommonConstants.GLOBAL_PROPERTY, config.key());
        return value.orElse(config.defaultValue());
    }
}
