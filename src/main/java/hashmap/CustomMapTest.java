package hashmap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/24
 */
public class CustomMapTest {
    public static void main(String[] args) {
        CustomMap<String, Integer> customMap = new CustomMap<>();
        customMap.put("张一", 10);
        customMap.put("李二", 20);
        customMap.put("张三", 30);

        System.out.println(customMap.get("张三"));


        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("张一", 10);
        hashMap.put("李二", 20);
        hashMap.put("张三", 30);

        System.out.println(hashMap);

        CustomMap<Integer, Integer> customMap2 = new CustomMap<>();
        for (int i = 0; i < 20; i++) {
            customMap2.put(i, i);
        }

        for (int i = 0; i < 20; i++) {
            System.out.println(customMap2.get(i));
        }

    }
}
