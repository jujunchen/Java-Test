import junit.framework.TestSuite;
import org.junit.Test;

import	java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/10/26
 */
public class ReentrantLockTest extends TestSuite {

    volatile int a = 0;

    @Test
    public void test1() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        Condition condition = lock.newCondition();

        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "竞争锁");
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "获取锁");

            while (a != 1) {
                try {
                    System.out.println(Thread.currentThread().getName() + "阻塞等待a == 1");
                    condition.await();
                    System.out.println("1111");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "释放锁");
            lock.unlock();
        });


        Thread t2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "竞争锁");
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "获取锁");

            a = 1;
            condition.signal();
            System.out.println(Thread.currentThread().getName() + "释放锁");
            lock.unlock();
        });

        t1.start();
        t2.start();

        Thread.sleep(1000);
    }

}
