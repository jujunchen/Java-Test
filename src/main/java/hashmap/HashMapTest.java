package hashmap;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/11/28
 */
public class HashMapTest {

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        for (int i = 1; i < 14; i++) {
            System.out.println("第"+i+"个");
            map.put(i + "", i + "");
        }
    }
}
