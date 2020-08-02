import drools.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jujun chen
 * @description: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 * @date: 2019/4/7
 */
public class HeapOOM {
    static class OOMObject{}

    public static void main(String[] args) {
        /*List<OOMObject> list = new ArrayList<OOMObject>();
        while (true){
            list.add(new OOMObject());
        }*/

        List<Object> objects = new ArrayList<> ();
        while (true) {
            objects.add(new Object());
        }

    }
}
