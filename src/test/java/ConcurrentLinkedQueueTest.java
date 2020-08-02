import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author jujun chen
 * @date 2020/07/09
 */
public class ConcurrentLinkedQueueTest {

    static ConcurrentLinkedQueue<Integer> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();

    static ConcurrentLinkedQueue<Integer> concurrentLinkedQueue1 = new ConcurrentLinkedQueue<>(Arrays.asList(1,2,3,4,5,6,7));

    static ConcurrentLinkedQueue<Person> concurrentLinkedQueue2 = new ConcurrentLinkedQueue<>();

    static Executor executor = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        concurrentLinkedQueue2.add(new Person());
        concurrentLinkedQueue2.add(new Person());
        concurrentLinkedQueue2.add(new Person());

        Person[] persons = new Person[4];
        Person[] newPersons = concurrentLinkedQueue2.toArray(persons);
    }

    @Data
    static class Person {
        String name;
        int age;
    }


    @Test
    public void test1() {
        first:
        for (;;) {
            for (int a = 0; a < 100; a++) {
                System.out.println(a);
                if (a%5 == 0) {
                    System.out.println("a%5");
                    continue first;
                }
            }
        }
    }


    @SneakyThrows
    @Test
    public void test() {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Spliterator<Integer> spliterator = concurrentLinkedQueue1.spliterator();
/*        spliterator.tryAdvance(System.out::println);
        spliterator.tryAdvance(System.out::println);
        spliterator.tryAdvance(System.out::println);*/
//        spliterator.forEachRemaining(System.out::println);
        //无限队列
//        System.out.println(spliterator.estimateSize());
//        System.out.println(spliterator.characteristics());
//        spliterator.tryAdvance(System.out::println);
//        System.out.println("----");
//        spliterator.forEachRemaining(System.out::println);
/*        executor.execute(() -> {
            Spliterator<Integer> childSpliterator = spliterator.trySplit();
            childSpliterator.forEachRemaining(e -> {
                System.out.println(Thread.currentThread().getName() + "=" + e);
            });
            countDownLatch.countDown();
        });

        executor.execute(() -> {
            Spliterator<Integer> childSpliterator = spliterator.trySplit();
            childSpliterator.forEachRemaining(e -> {
                System.out.println(Thread.currentThread().getName() + "=" + e);
            });
            countDownLatch.countDown();
        });

        executor.execute(() -> {
            Spliterator<Integer> childSpliterator = spliterator.trySplit();
            childSpliterator.forEachRemaining(e -> {
                System.out.println(Thread.currentThread().getName() + "=" + e);
            });
            countDownLatch.countDown();
        });
        countDownLatch.await();*/
         /* Spliterator<Integer> childSpliterator2 = spliterator.trySplit();
        childSpliterator2.forEachRemaining(System.out::println);

        Spliterator<Integer> childSpliterator3 = spliterator.trySplit();
        childSpliterator3.forEachRemaining(System.out::println);*/

//        Object[] objects = concurrentLinkedQueue.toArray();
//        Spliterator spliterator = concurrentLinkedQueue.spliterator();
//        Spliterator spliterator1 = spliterator.trySplit();
//        spliterator1.forEachRemaining(System.out::println);
    }
}
