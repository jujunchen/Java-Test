import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author: jujun chen
 * @Type AtomicTest
 * @description:
 * @date: 2019/06/11
 */
public class AtomicTest extends TestSuite {
    private static AtomicBoolean atomicBoolean = new AtomicBoolean(true);

    private static Executor executor = Executors.newFixedThreadPool(20);

    @Test
    public void atomicBooleanTest() throws InterruptedException {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                atomicBoolean.lazySet(false);
            }
        });
        thread1.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(atomicBoolean.get());
            }
        });
        thread2.start();

        Thread.sleep(1000);
    }
    
    
    @Test
    public void atomicIntegerTest() {
        AtomicInteger atomicInteger = new AtomicInteger(1);

        int result = atomicInteger.getAndUpdate(e -> e + 3);
        ////返回计算前结果
        assert result == 1;
        //1 + 3
        assert atomicInteger.get() == 4;


        result = atomicInteger.updateAndGet(e -> e + 3);
        //返回计算后结果
        assert result == 7;
        // 4 + 3
        assert atomicInteger.get() == 7;


        result = atomicInteger.getAndAccumulate(10, (x, y) -> x + y);
        //返回计算前结果
        assert  result == 7;
        // 7 + 10
        assert atomicInteger.get() == 17;

        result = atomicInteger.accumulateAndGet(10, (x, y) -> x + y);
        //返回计算后的结果
        assert result == 27;
        assert atomicInteger.get() == 27;
    }

    @Test
    public void atomicIntegerArrayTest() {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(10);
        int result = atomicIntegerArray.getAndUpdate(0, e -> e + 1);
        assert result == 0;
        // 0 + 1
        assert atomicIntegerArray.get(0) == 1;

        result = atomicIntegerArray.updateAndGet(0, e -> e + 1);
        assert result == 2;
        //1 + 1
        assert atomicIntegerArray.get(0) == 2;

        result = atomicIntegerArray.getAndAccumulate(0, 3, (x, y) -> x + y);
        assert result == 2;
        // 2 + 3
        assert atomicIntegerArray.get(0) == 5;
    }

    @Test
    public void fieldUpdaterTest() {
        AtomicIntegerFieldUpdater fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Person.class, "count");
        Person person = new Person("小明", "男");
        fieldUpdater.addAndGet(person, 10);
        System.out.println(fieldUpdater.get(person));
        System.out.println(person);

        AtomicLongFieldUpdater fieldUpdater1 = AtomicLongFieldUpdater.newUpdater(Person.class, "age");
        Person person1 = new Person("小明", "男");
        fieldUpdater1.addAndGet(person1, 10);
        System.out.println(fieldUpdater1.get(person1));
        System.out.println(person1);
    }


    @Test
    public void doubleAccumulatorTest() {
        DoubleAccumulator doubleAccumulator = new DoubleAccumulator((x,y) -> x + y, 10);
        doubleAccumulator.accumulate(10);
        System.out.println(doubleAccumulator.get());
    }

    @Test
    public void longAdderTest() {
        LongAdder longAdder = new LongAdder();
        longAdder.add(101);
        longAdder.add(102);
        //203
        System.out.println(longAdder.sumThenReset());
        //0
        System.out.println(longAdder.longValue());
    }


    @Test
    public void multiplyThreadLongAdderTest() throws InterruptedException {
        LongAdder longAdder = new LongAdder();
        AtomicLong atomicLong = new AtomicLong();

        AtomicLong time1 = new AtomicLong();
        AtomicLong time2 = new AtomicLong();

        int threadNum = 5;
        int cycleNums = 500000;
        CountDownLatch countDownLatch1 = new CountDownLatch(threadNum);
        for (int a = 0; a < threadNum; a++) {
            executor.execute(() -> {
                long start = System.nanoTime();
                for (int i = 0; i < cycleNums; i++) {
                    longAdder.increment();
                }
                //System.out.println(longAdder.longValue());
                time1.addAndGet(System.nanoTime() - start);
                countDownLatch1.countDown();
            });
        }
        countDownLatch1.await();

        CountDownLatch countDownLatch2 = new CountDownLatch(threadNum);
        for (int a = 0; a < threadNum ; a++) {
            executor.execute(() -> {
                long start = System.nanoTime();
                for (int i = 0; i < cycleNums; i++) {
                    atomicLong.incrementAndGet();
                }
                //System.out.println(atomicLong.longValue());
                time2.addAndGet(System.nanoTime() - start);
                countDownLatch2.countDown();
            });
        }
        countDownLatch2.await();

        System.out.println("data=" + longAdder.longValue() + " LongAdder time1 = " + time1.longValue());
        System.out.println("data=" + atomicLong.longValue() + " AtomicLong time2 = " + time2.longValue());
    }

    @Test
    public void test123() {
        Person person = null;
        System.out.println(Optional.ofNullable(person).orElseGet(() -> new Person("aa", "男")));
    }



    private static class Person{
        String name;
        String sex;
        volatile int count;
        volatile long age;

        public Person(String name, String sex) {
            this.name = name;
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    ", count=" + count +
                    ", age=" + age +
                    '}';
        }
    }
    

}

