import junit.framework.TestSuite;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author: jujun chen
 * @description:
 * @date: 2020-08-08
 */
public class JVMTest {


    @Test
    public void test1() {
        ArrayList<Object> objects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            byte[] bytes = new byte[1024 * 1024];
            objects.add(bytes);
            if (objects.size() == 3)
                objects.clear();
        }
    }
}
