package com.ixigo.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dixant on 29/03/17.
 */
public final class CacheManager {
    private static CacheManager _INSTANCE = new CacheManager();

    private Map<Class, AbstractCache> map;

    private CacheManager() {
        map = new HashMap<>();
    }

    public static CacheManager getInstance() {
        return _INSTANCE;
    }

    public <T extends AbstractCache> T getCache(Class<T> clazz) {
        return (T) map.get(clazz);
    }

    public <T extends AbstractCache> void putCache(T cache) {
        map.put(cache.getClass(), cache);
    }

}
