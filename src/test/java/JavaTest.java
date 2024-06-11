import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import junit.framework.TestSuite;
import lombok.*;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
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
    public void test3() {
        String[] space1 = new String[3];
        String[] space = ArrayUtils.add(space1, "001");
    }

    
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


    @Test
    public void test2() {
        List<String> stringList = new ArrayList<>();
        System.out.println(stringList.get(0));
    }

    @Test
    public void test4() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);

        integerList = integerList.stream().map(i -> {return i +1;}).collect(Collectors.toList());
        System.out.println(integerList);
    }


    @Test
    public void test5() {
        TreeSet treeSet = new TreeSet<Person>(Comparator.comparing(Person::getAge));
        Person person0 = Person.builder().age(19).build();
        Person person = Person.builder().age(10).build();
        Person person1 = Person.builder().age(11).build();
        Person person2 = Person.builder().age(12).build();
        Person person3 = Person.builder().age(12).build();
        treeSet.add(person0);
        treeSet.add(person);
        treeSet.add(person1);
        treeSet.add(person2);
        treeSet.add(person3);
        System.out.println(treeSet);
    }


    @Test
    public void test6() {
        List<Person> personList = new ArrayList<>();
        Person person0 = Person.builder().age(19).build();
        Person person = Person.builder().age(10).build();
        Person person1 = Person.builder().age(11).build();
        Person person2 = Person.builder().age(12).build();
        Person person3 = Person.builder().age(12).build();
        personList.add(person0);
        personList.add(person);
        personList.add(person1);
        personList.add(person2);
        personList.add(person3);
        Map<Integer, Person> map = personList.parallelStream().collect(Collectors.toMap(Person::getAge, Function.identity(), (k1,k2) -> k1));
        System.out.println(map);
    }


    @Test
    public void test7() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("appId","d29b40350c294bdbb1c0d0275f6b1649");
//        params.put("projectIdThird", "wlsq01");
//        params.put("communityIdThird", "1000071");
//        params.put("eventSource", "LLT");
//        params.put("eventTypeThird", "公共维修");
//        params.put("eventTime", "2021-10-15 19:43:27");
//        params.put("position", "鼎创财富中心");
//        params.put("customerName", "火舞");
//        params.put("customerPhone", "13538489837");
//        params.put("description", "是他先动的手");
//        long timestamp = System.currentTimeMillis();
//        params.put("timestamp", timestamp);
//        params.put("appSecret", "0aa4cbf7480a43a98890f761c8563235");
//        params.put("urlSchemes", "A569UQ6CK4000");
//        System.out.println(MapUtil.sortJoin(params, "", "", false));
//        String newSign = MD5.create().digestHex(MapUtil.sortJoin(params, "", "", false));
//        System.out.println(timestamp);
//        System.out.println(newSign);
    }

    @Test
    public void test8() {
        long timestamp = System.currentTimeMillis();
        Map<String, Object> params = new HashMap<>();
        params.put("appId","da32fc61d59e490bbb7e85676d53ca5c");
        params.put("projectIdThird", "wlsq01");
        params.put("communityIdThird", "1000071");
        params.put("eventSource", "LLT");
        params.put("eventTypeThird", "公共维修");
        params.put("eventTime", "2021-10-15 19:43:27");
        params.put("position", "鼎创财富中心");
        params.put("customerName", "火舞");
        params.put("customerPhone", "13538489837");
        params.put("description", "是他先动的手");
        Voucher voucher = new Voucher();
        voucher.setStatus(1);
        params.put("bean", voucher);
        // JSONArray aryList = new JSONArray();
        // aryList.add("https://www.baidu.com/a.jpg");
        // aryList.add("https://www.baidu.com/b.jpg");
        // // String[] aryList = new String[2];
        // // aryList[0] = "https://www.baidu.com/a.jpg";
        // // aryList[1] = "https://www.baidu.com/b.jpg";
        // params.put("picUrls", aryList);
        // params.put("timestamp", timestamp);
        // params.put("appSecret", "f648a7aab92f450a967e7831405656b9");
        // params.put("urlSchemes", "A17GC6UG902800");

        //引入Hutool 新版本，老版本没有该方法
//        String json = JSONObject.toJSONString(params);
//        Map<String,Object> map = JSONObject.parseObject(json).getInnerMap();
//        String str = MapUtil.sortJoin(map, "", "", false);
//        System.out.println(str);
        // String newSign = MD5.create().digestHex(str); 
        // System.out.println(str + "_" + newSign);

        // Map<String, Object> body = new HashMap<>();
        // body.put("appId","da32fc61d59e490bbb7e85676d53ca5c");
        // body.put("projectIdThird", "wlsq01");
        // body.put("communityIdThird", "1000071");
        // body.put("eventSource", "LLT");
        // body.put("eventTypeThird", "公共维修");
        // body.put("eventTime", "2021-10-15 19:43:27");
        // body.put("position", "鼎创财富中心");
        // body.put("customerName", "火舞");
        // body.put("customerPhone", "13538489837");
        // body.put("description", "是他先动的手");
        // // body.put("picUrls", aryList);
        // body.put("timestamp", timestamp);
        // body.put("sign", newSign);

        // HttpRequest httpRequest = HttpRequest.post("localhost:7013/open/event/push");
        // String result = httpRequest.body(JSONObject.toJSONString(body)).execute().body();
        // System.out.println(JSONObject.toJSONString(body));
        // System.out.println(result);
    }
    
    
    @Test
	public void test18() {
    	String json = "{\"wjy-flowmeter-1\":{\"instantaneous delivery\":\"0.0000\",\"Instantaneous heat flow\":\"0.0000\",\"fluid velocity\":\"0.0000\",\"Measure fluid sound velocity\":\"1328.1700\"},\"Equipment to distinguish\":{\"differentiate-1\":\"123\",\"differentiate-2\":\"0\",\"differentiate-3\":\"11\",\"differentiate-4\":\"0\",\"differentiate-5\":\"22\",\"differentiate-6\":\"0\",\"differentiate-7\":\"33\",\"differentiate-8\":\"0\",\"differentiate-9\":\"44\",\"differentiate-10\":\"0\"}}";
    	JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(json);
    	jsonObject.forEach((k,v) -> {
    		
    	});
	}
    
    @Test
	public void name() {
    	Voucher voucher = new Voucher();
    	//null == 1  报NPE
    	assert voucher.getStatus() == 1;
	}
    
    @Test
	public void snowIdTest() {
        String snowflakeId = IdUtil.getSnowflake(0, 0).nextIdStr();
        String id = DateUtil.format(new Date(System.currentTimeMillis()), "yyyyMMddHHmmssSSS") + snowflakeId.substring(15);
        System.out.println(id);
	}


    @Test
    public void moveExcel() {
        String a = "MD";
        System.out.println(a.getBytes()[1]);
    }


    @Test
    public void foreachTest() {
        List<String> stringList = new ArrayList<>();
        stringList.add("a");
        stringList.add("b");
        stringList.add("c");

        for (String str : stringList) {
            if (str.equals("b")) {
                stringList.remove("b");
            }
        }

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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Person {
    private String name;
    private Integer age;
}