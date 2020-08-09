import junit.framework.TestSuite;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author: jujun chen
 * @description:
 * @date: 2020-08-08
 */
public class JVMTest {


    /**
     * -Xms -Xmx -Xmn对GC回收次数的影响
     * -Xms11M -Xmx11M -Xmn2M
     */
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

    //-Xss -Xmx 对线程数量的影响
    //本地测试，线程数量一直没有变化
    //-Xms100M -Xmx100M -Xss1M
    @Test
    public void test2() {
        int i = 0;
        try {
            for (; i < 10000; i++) {
                new Thread(() -> {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception | OutOfMemoryError ex) {
            ex.printStackTrace();
            System.out.println("thread count: " + i);
        }
    }

}
