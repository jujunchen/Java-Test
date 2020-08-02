import junit.framework.TestSuite;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/08/26
 */
public class ArrayBlockingQueueTest extends TestSuite {

    private static ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(3);

    @Test
    public void putTest() throws InterruptedException {
        arrayBlockingQueue.put("a");
        arrayBlockingQueue.put("b");
        arrayBlockingQueue.put("c");

        Thread thread1 = new Thread(() -> {
            try {
                arrayBlockingQueue.put("d");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            arrayBlockingQueue.remove("c") ;
        });

        thread1.start();
        thread2.start();

        Thread.sleep(10000);
        System.out.println(arrayBlockingQueue);
    }

}
