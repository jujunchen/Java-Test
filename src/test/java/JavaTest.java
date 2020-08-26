import junit.framework.TestSuite;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import	java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import	java.util.concurrent.Exchanger;
import	java.util.HashMap;
import	java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/08/21
 */
public class JavaTest extends TestSuite {

    @BeforeClass
    public static void init() {
        System.out.println("初始化");
    }

    @AfterClass
    public static void destroy() {
        System.out.println("销毁");
    }

    private static Executor executor = Executors.newFixedThreadPool(5);

    
    @Test
    public void hasUniqueObjectTest() {

        List<Integer> integerList = Arrays.asList(1,2,1,3);

        boolean hasUnique = CollectionUtils.hasUniqueObject(integerList);
        System.out.println(hasUnique);
        
    }


    @Test
    public void mathTest() {
        double a = 3.6001;
        System.out.println(Math.ceil(a));
    }

    @Test
    public void threadKeyTest() {
        int HASH_INCREMENT = 0x61c88647;
        this.getClass();
        AtomicInteger nextValue = new AtomicInteger();
        nextValue.getAndAdd(HASH_INCREMENT);
        int nextHash = nextValue.get();

        int len = 16;

        int key = nextHash & (len - 1);
        System.out.println(key);
    }

    //多线程产生随机数
    @SneakyThrows
    @Test
    public void randomTest1() {
        class RandomThread1 extends Thread {
            @Override
            public void run() {
                Random random = new Random();
                System.out.println(random.nextInt(10));
            }
        }

        new RandomThread1().start();
        new RandomThread1().start();
        TimeUnit.SECONDS.sleep(3);
    }


    @Test
    //测试random多线程情况下产生相同的随机数
    public void RandomTest() throws InterruptedException {
        executor.execute(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("线程1=" + (int)(Math.random() * 10));
            }
        });

        executor.execute(() -> {
            for (int i = 0; i < 100 ; i++) {
                System.out.println("线程2=" + (int)(Math.random() * 10));
            }
        });

        Thread.sleep(5000);
    }
    
    @Test
    public void threadLocalRandomTest() throws InterruptedException {
        executor.execute(() -> {
            System.out.println(Thread.currentThread().getName());
            ThreadLocalRandom random = ThreadLocalRandom.current();
            System.out.println(random);
            for (int i = 0; i < 100 ; i++) {
                System.out.println("线程1=" + random.nextInt(10));
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executor.execute(() -> {
            System.out.println(Thread.currentThread().getName());
            ThreadLocalRandom random = ThreadLocalRandom.current();
            System.out.println(random);
            for (int i = 0; i < 100 ; i++) {
                System.out.println("线程2=" + random.nextInt(10));
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        Thread.sleep(5000);
    }


    //单例测试
    @Test
    public void singleTest() {
        executor.execute(() -> {
            Single single = Single.getInstance();
            System.out.println(Thread.currentThread().getName() + "=" + single);
        });

        executor.execute(() -> {
            Single single = Single.getInstance();
            System.out.println(Thread.currentThread().getName() + "=" + single);
        });

    }


    @Test
    public void durationTest() {
        Duration duration = Duration.ofSeconds(1000);
        System.out.println(duration.toMinutes());
    }


    @Test
    public void hashMapThreadTest() throws InterruptedException {
        final HashMap<String, String> map = new HashMap<>();

        Thread t = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                new Thread(() -> {
                    map.put(UUID.randomUUID().toString(), "");
                }, "ftf" + i).start();
            }
        });
        t.start();
        t.join();
        System.out.println(map.size());
    }


    @Test
    public void hashMapTest() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        Map<String, String> map0 = new HashMap<>(0);
        map0.put("0", "0");
        Map<String, String> map1 = new HashMap<>(2);
        map1.put("2", "2");
        map1.put("2-1", "2-1");
        Map<String, String> map2 = new HashMap<>(2, 0.5f);
        map2.put("3", "3");

    }


    @Test
    public void equlasTest() {
        int a = 1;
        Integer b = new Integer(1);
        Integer c = new Integer(1);
        Integer d = Integer.valueOf(1);
        Long e = new Long(1);
        Long f = Long.valueOf(1);

        assert a == b;
        assert b != c;
        assert b != d;
        assert a == d;
        assert a == e;
        assert a == f;
        assert b.equals(c);
        assert !b.equals(e);
    }

    @Test
    public void semaphoreTest() throws InterruptedException {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("输出");
                    //semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        TimeUnit.SECONDS.sleep(3);
        System.out.println("释放许可证===");
        semaphore.release(2);
    }


    @Test
    public void exchangerTest() throws InterruptedException {
        Exchanger<Integer> exchanger = new Exchanger<> ();

        Thread thread = new Thread(() -> {
            Integer a = 1;
            try {
                Integer b = exchanger.exchange(a);
                System.out.println("Thread1：" + b);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        Integer c = exchanger.exchange(2);
        Thread.sleep(3000);
        System.out.println("Thread2:" + c);
    }


    //nio buffer test
    @Test
    public void bufferTest() {
        IntBuffer intBuffer = IntBuffer.allocate(10);
        for (int i = 0; i < 5; i++) {
            intBuffer.put(i);
        }
        //重置后，后面不能再写入数据
        intBuffer.flip();

        /*for (int i = 0; i < 6; i++) {
            intBuffer.put(i);
        }*/

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }

        System.out.println("=============");
        //要能重新可以写入,会将未读取的数据copy到开头
        intBuffer.compact();
        for (int i = 0; i < 5; i++) {
            intBuffer.put(i);
        }

        //操作读取后, limit 没变
        intBuffer.rewind();

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());;
        }
    }
    
    
    @Test
    public void fileChannelTest() {
        String resourceName = this.getClass().getResource("").getPath();
        try(RandomAccessFile accessFile = new RandomAccessFile(resourceName + "nio-data.txt", "rw")) {
            FileChannel fileChannel = accessFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(48);
            int bytesRead = fileChannel.read(buffer);
            while (bytesRead != -1) {
                System.out.println("Read " + bytesRead);
                buffer.flip();

                while (buffer.hasRemaining()) {
                    System.out.println((char) buffer.get());
                }
                buffer.clear();

                bytesRead = fileChannel.read(buffer);
            }
        } catch (Exception e) {

        }
    }


    @Test
    public void fileChannelTest2() {
        String resourceName = this.getClass().getResource("").getPath();
        try(RandomAccessFile fromFile = new RandomAccessFile(resourceName + "nio-data.txt", "rw");
            RandomAccessFile toFile = new RandomAccessFile(resourceName + "nio-data2.txt", "rw")
        ) {
            FileChannel fromFileChannel = fromFile.getChannel();
            FileChannel toFileChannel = toFile.getChannel();

            long position = 0;
            long count = fromFileChannel.size();
            toFileChannel.transferFrom(fromFileChannel, position, count);
            System.out.println("toFileChannel:" + toFileChannel.size());
        } catch (Exception e) {

        }

    }


    @Test
    public void subStrName() {
        String method = "setTestName";
        String property = method.length() > 3 ? method.substring(3, 4).toLowerCase() + method.substring(4) : "";
        System.out.println(property);
    }
    
    @Test
    public void luojiyu() {
        int a = 3;
        int b = 4;
        int c = 0;
        if (a == 3 & (c = a + b) == 7) {
            System.out.println("c=" + c);
        }
    }


    /**
     * 枚举实现的单例
     */
    @Test
    public void enumSingleTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Person p1 = SingleEnum.INSTANCE.getInstance();
        Person p2 = SingleEnum.INSTANCE.getInstance();

        System.out.println(p1);
        System.out.println(p2);

        Constructor<SingleEnum> constructor = SingleEnum.class.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        SingleEnum single = constructor.newInstance("test");
        System.out.println(single);
    }


    @Test
    public void stringTokenTest() {
        StringTokenizer stringTokenizer = new StringTokenizer("abc,def", ",");
        System.out.println(stringTokenizer.countTokens());

        while (stringTokenizer.hasMoreElements()) {
            System.out.println(stringTokenizer.nextToken());
        }
    }
    
    @Test
    public void ff() {
        Voucher voucher = new Voucher();
        voucher.setStatus(1);

        Voucher2 voucher2 = new Voucher2();
        BeanUtils.copyProperties(voucher, voucher2);
        System.out.println(voucher2);
    }

    /**
     * 让一段程序并发的执行，并最终汇总结果
     */
      //cyclicBarrier
    @Test
    public void cyclicBarrierTest() throws BrokenBarrierException, InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        AtomicInteger result = new AtomicInteger();
        executor.execute(() -> {
            try {
                int result1 = 0;
                for (int i = 0; i < 100; i++) {
                    result1 += i;
                }
                cyclicBarrier.await();
                result.addAndGet(result1);
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        executor.execute(() -> {
            try {
                int result1 = 0;
                for (int i = 0; i < 100; i++) {
                    result1 += i;
                }
                cyclicBarrier.await();
                result.addAndGet(result1);
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        cyclicBarrier.await();
        //9900
        System.out.println(result);
    }


    @Test
    public void longStream() {
        Instant start = Instant.now();
        long result = LongStream.rangeClosed(0, 100L).parallel().reduce(0, Long::sum);
        Instant end = Instant.now();
        System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");

        System.out.println("结果为：" + result);
    }
    
    @Test
    public void bitCalculation() {
        int a = 100;
        System.out.println(a << 1);
        System.out.println(a >> 1);
    }

    /**
     * removeIf测试
     */
    @Test
    public void removeIfTest() {
        List<String> stringList = new ArrayList<>(){
            {
                add("a");
                add("b");
                add("c");
            }
        };

        stringList.removeIf(item -> item.equals("b"));
        System.out.println(stringList);
    }


}

@Data
class Voucher {
    private Integer status;
}

@Data
class Voucher2 {
    private Integer[] status;
}






class Single {
    static Single instance = new Single();

    public static Single getInstance() {
        return instance;
    }
}


enum SingleEnum {
    INSTANCE;

    private Person person;

    private SingleEnum() {
        person = new Person();
    }

    public Person getInstance() {
        return person;
    }
}


class Person {
    private String name;
}