package org.dixantmittal.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by dixant on 29/03/17.
 */
public abstract class AbstractCache<K, V> {
    protected Map<K, V> map;

    public AbstractCache() {
        map = new HashMap<K, V>();
    }

    public void put(K key, V val) {
        map.put(key, val);
    }

    public V get(K key) {
        return map.get(key);
    }

    public V getOrDefault(K key, V defaultValue) {
        V val;
        return (val = map.get(key)) != null
                ? val : defaultValue;
    }
    
    public V computeIfAbsent(K key, Function<? super K,? extends V> mappingFunction) {
    	if(map.get(key) == null) {
    		map.put(key, mappingFunction.apply(key));
    	}
    	return map.get(key);
    }
}
