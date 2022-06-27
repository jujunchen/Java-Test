import junit.framework.TestSuite;
import org.junit.Test;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/10/26
 */
public class LinkedBlockingQueueTest extends TestSuite {

    Executor executor = Executors.newFixedThreadPool(3);

    @Test
    public void test1() throws InterruptedException {
        LinkedBlockingQueue blockingQueue = new LinkedBlockingQueue();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        executor.execute(() -> {
            for (int i = 0; i < 1000; i++) {
                blockingQueue.offer(i);
            }
        });

        executor.execute(() -> {
            for (int i = 0; i < 2000; i++) {
                blockingQueue.offer(i);
            }
        });

        executor.execute(() -> {
            for (int i = 0; i < 3000; i++) {
                blockingQueue.offer(i);
            }
            countDownLatch.countDown();
        });



        System.out.println(blockingQueue.size());
        System.out.println(blockingQueue);

        countDownLatch.await();
        System.out.println(blockingQueue.size());
    }
}
