package hashmap;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/24
 */
public class CustomMap<K,V> {

    private static final int DEFAULT_CAPACITY = 1 << 4;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int size;

    private transient Node<K, V>[] tab;

    @SuppressWarnings({"rawtypes","unchecked"})
    public CustomMap() {
        tab = new Node[DEFAULT_CAPACITY];
    }

    static class Node<K,V> {
        K key;
        V value;
        Node<K,V> next;

        private Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return "key=" + key + ", value=" + value;
        }
    }

    public V put(K key, V value) {
        if (size >= DEFAULT_CAPACITY * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = hash(key) & (tab.length - 1);
        if (tab [index] == null) {
            tab[index] = new Node<K, V> (key, value, null);
        } else {
            Node<K,V> node = tab [index];
            Node<K, V> next;
            while ((next = node.next) != null) {
                if (next.key == key || next.key.equals(key)) {
                    next.value = value;
                   return next.value;
                }
            }
            node.next = new Node<>(key, value, null);
        }
        ++size;
        return null;
    }

    public V get(K key) {
        int index = hash(key) & (tab.length - 1);
        Node<K,V> node = tab[index];
        if (node != null) {
            Node<K,V> next;
            while ((next = node.next) != null) {
                if (next.key == key || next.key.equals(key)) {
                    return next.value;
                }
            }
            return node.value;
        }
        return null;
    }

    private void resize() {
        int newLength = 2 * DEFAULT_CAPACITY;
        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K, V>[] newTab = new Node[newLength];
        for (int i = 0; i < tab.length; i++) {
            Node<K,V> e;
            if ((e = tab[i]) != null) {
                tab[i] = null;
                if (e.next == null) {
                    newTab[hash(e.key) & (newTab.length - 1)] = e;
                } else {
                    Node<K,V> next;
                    while ((next = e.next) != null) {
                        int index = hash(next.key) & (newTab.length - 1);
                        Node<K,V> newNode;
                        if ((newNode = newTab[index]) != null) {
                            newNode.next = new Node<>(next.key, next.value, null);
                        }
                    }
                }
            }
        }
        tab = newTab;
    }

    private int hash(K key) {
        int h = 0;
        return key == null ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }


}


