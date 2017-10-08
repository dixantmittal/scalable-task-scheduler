package org.dixantmittal.cache;

import java.util.Map;
import java.util.Set;

/**
 * Created by dixant on 06/04/17.
 */
public class ProducerPropertiesCache extends AbstractCache<String, String> {
    public Set<Map.Entry<String, String>> entrySet() {
        return map.entrySet();
    }
}
