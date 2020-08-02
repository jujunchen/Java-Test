import junit.framework.TestSuite;
import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/06/20
 */
public class LockSupportTest extends TestSuite {

    @Test
    public void packUnpackTest() {
        System.out.println("start...");
        LockSupport.unpark(Thread.currentThread());

        LockSupport.park();
        System.out.println("end...");
    }


    @Test
    public void test1() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("child Thread park");
            LockSupport.park();
            System.out.println("child Thread park end");
        });

        thread.start();
        LockSupport.unpark(thread);
        System.out.println("main thread unpark");
        thread.join();

    }
}
