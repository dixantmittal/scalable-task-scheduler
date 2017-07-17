package org.dixantmittal.cache.impl;

import org.dixantmittal.cache.AbstractCache;

import java.util.Map;
import java.util.Set;

/**
 * Created by dixant on 06/04/17.
 */
public class ConsumerPropertiesCache extends AbstractCache<String, String> {
    public Set<Map.Entry<String, String>> entrySet() {
        return map.entrySet();
    }
}
