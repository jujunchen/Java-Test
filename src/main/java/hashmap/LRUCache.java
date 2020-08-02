package hashmap;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: jujun chen
 * @Type
 * @description: 实现一个LRU
 * @date: 2019/12/13
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private int initialCapacity;

    public LRUCache(int initialCapacity) {
        super(initialCapacity, 0.75f, true);
        this.initialCapacity = initialCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        if (size() > initialCapacity) {
            return true;
        }
        return false;
    }
}
