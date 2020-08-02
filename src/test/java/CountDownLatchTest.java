import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import	java.util.concurrent.ExecutorService;
import	java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/08/26
 */
public class CountDownLatchTest {

    public static final CountDownLatch countDownLatch = new CountDownLatch(2);

    ExecutorService executor = Executors.newFixedThreadPool(2);

    @Test
    public void test1() throws InterruptedException {
        executor.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " step1");
            countDownLatch.countDown();
        });
        executor.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " step2");
            countDownLatch.countDown();
        });

        countDownLatch.await();
        System.out.println("thread end");
    }
}
