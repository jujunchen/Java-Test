import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/10/13
 */
public class CyclicBarrierTest {

    public static final CyclicBarrier cycle = new CyclicBarrier(3);

    ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Test
    public void test1() throws BrokenBarrierException, InterruptedException {
        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                cycle.await();
                System.out.println(Thread.currentThread().getName() + "，执行结束");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
                cycle.await();
                System.out.println(Thread.currentThread().getName() + "，执行结束");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        System.out.println(Thread.currentThread().getName());
        cycle.await();
        System.out.println(Thread.currentThread().getName() + "，执行结束");
    }
}
