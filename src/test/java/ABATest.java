import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author jujun chen
 * @date 2020/07/03
 */
public class ABATest {

    @SneakyThrows
    @Test
    public void test1() {
        AtomicInteger atomicInteger = new AtomicInteger(10);

        CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(() -> {
            atomicInteger.compareAndSet(10, 11);
            atomicInteger.compareAndSet(11,10);
            System.out.println(Thread.currentThread().getName() + "：10->11->10");
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                boolean isSuccess = atomicInteger.compareAndSet(10,12);
                System.out.println("设置是否成功：" + isSuccess + ",设置的新值：" + atomicInteger.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }).start();

        countDownLatch.await();
    }


    //AtomicStampedReference,通过维护一个版本号
    @SneakyThrows
    @Test
    public void test2() {
        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference(10,1);

        CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(() -> {
            //使用第一次获取的版本，因为不知道有其他线程偷摸改了
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + " 第一次版本：" + stamp);
            try {
                //等待一下
                TimeUnit.SECONDS.sleep(2);
                //这个线程打算修改10->12
                boolean isSuccess = atomicStampedReference.compareAndSet(10,12, stamp, stamp + 1);
                System.out.println(Thread.currentThread().getName() + " 修改是否成功：" + isSuccess + " 当前版本：" + atomicStampedReference.getStamp() + " 当前值：" + atomicStampedReference.getReference());
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            //这个线程偷摸的把10->11->10
            System.out.println(Thread.currentThread().getName() + " 第一次版本：" + atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(10, 11, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + " 第二次版本：" + atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(11, 10, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + " 第三次版本：" + atomicStampedReference.getStamp());
            countDownLatch.countDown();
        }).start();


        countDownLatch.await();
    }

    @SneakyThrows
    @Test
    public void test3() {
        AtomicMarkableReference<Integer> markableReference = new AtomicMarkableReference<>(10, false);

        CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 第一次标记：" + markableReference.isMarked());
            markableReference.compareAndSet(10, 11, false, true);
            System.out.println(Thread.currentThread().getName() + " 第二次标记：" + markableReference.isMarked());
            markableReference.compareAndSet(11, 10, true, false);
            System.out.println(Thread.currentThread().getName() + " 第三次标记：" + markableReference.isMarked());
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 第一次标记：" + markableReference.isMarked());
            try {
                TimeUnit.SECONDS.sleep(2);
                boolean isSuccess = markableReference.compareAndSet(10,12, false, true);
                System.out.println(Thread.currentThread().getName() + " 修改是否成功：" + isSuccess + " 当前标记：" + markableReference.isMarked() + " 当前值：" + markableReference.getReference());
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        countDownLatch.await();
    }


    @SneakyThrows
    @Test
    public void test4() {
        AtomicMarkableReference<Integer> markableReference = new AtomicMarkableReference<>(10,true);

        CountDownLatch countDownLatch = new CountDownLatch(2);

        System.out.println("初始标记: " + markableReference.isMarked());


        //该线程将10设置为逻辑删除
        new Thread(() -> {
            boolean isSuccess = markableReference.attemptMark(10,false);
            System.out.println("设置标记为flase: " + isSuccess + "，当前标记："+ markableReference.isMarked());
            countDownLatch.countDown();
        }).start();

        //该线程想要更新10->11，但标志已经变为false，与预期不符
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean isSuccess = markableReference.compareAndSet(10, 11, true, false);
            System.out.println("设置值: " + isSuccess + "期望标记：true"  + "，当前标记："+ markableReference.isMarked());
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();
    }
}
