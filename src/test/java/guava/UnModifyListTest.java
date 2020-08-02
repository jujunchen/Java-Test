package guava;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import junit.framework.TestSuite;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/07/25
 */
public class UnModifyListTest extends TestSuite {
    
    @Test
    public void immutableListTest() {
        ImmutableList immutableList = ImmutableList.copyOf(Arrays.asList("a", "b"));
        immutableList.add("123");
    }
    
    
    @Test
    public void immutableMap() {
        BiMap<Integer, String> biMap = HashBiMap.create();
        biMap.put(1, "a");
        biMap.put(2, "b");
        biMap.put(3, "c");

        BiMap<String, Integer> inverseMap = biMap.inverse();
        System.out.println("inverseMap:" + inverseMap);

        inverseMap.put("d", 4);
        System.out.println("inverseMap2:" + inverseMap);
        System.out.println("biMap:" + biMap);

        ImmutableBiMap immutableBiMap = ImmutableBiMap.copyOf(biMap);
        immutableBiMap.put(5, "e");
        
    }
    
    
    @Test
    public void unioTest() {
        HashSet set = Sets.newHashSet("a", "b", "c");
        HashSet set1 = Sets.newHashSet("d", "e", "f", "a");
        Sets.SetView sets = Sets.union(set, set1);
        System.out.println(sets);

        //返回set1中不包含set的元素
        Sets.SetView setView = Sets.difference(set, set1);
        System.out.println(setView);

        //返回元素的交集
        Sets.SetView setView1 = Sets.intersection(set, set1);
        System.out.println(setView1);
    }
    
}
