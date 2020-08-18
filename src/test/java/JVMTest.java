import lombok.SneakyThrows;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author: jujun chen
 * @description:
 * @date: 2020-08-08
 */
public class JVMTest {


    /**
     * -Xms -Xmx -Xmn对GC回收次数的影响
     * -Xms11M -Xmx11M -Xmn2M
     */
    @Test
    public void test1() {
        ArrayList<Object> objects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            byte[] bytes = new byte[1024 * 1024];
            objects.add(bytes);
            if (objects.size() == 3)
                objects.clear();
        }
    }

    //-Xss -Xmx 对线程数量的影响
    //本地测试，线程数量一直没有变化
    //-Xms100M -Xmx100M -Xss1M
    @Test
    public void test2() {
        int i = 0;
        try {
            for (; i < 10000; i++) {
                new Thread(() -> {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (Exception | OutOfMemoryError ex) {
            ex.printStackTrace();
            System.out.println("thread count: " + i);
        }
    }

    //STM对应用的影响
    @SneakyThrows
    @Test
    public void stwTest() {
        STMThread  stmThread = new STMThread();
        PrintThread printThread = new PrintThread();
        stmThread.start();
        printThread.start();

        Thread.sleep(100000);
    }

    @Test
    public void putInEden() {
        byte[] b1,b2,b3,b4;
        b1 = new byte[1024*512]; //0.5M
        b2 = new byte[1024*1024*4];  //4M
        b3 = new byte[1024*1024*4];
        b3 = null;
        b3 = new byte[1024*1024*4];

        // -XX:+DisableExplicitGC 禁用显示GC
//        System.gc();
    }

    @Test
    public void heapSize() throws InterruptedException {
        ArrayList<Object> objects = new ArrayList<>();
        while (true) {
            byte[] bytes = new byte[1024 * 1024];
            objects.add(bytes);
            if (objects.size() == 10)
                objects = new ArrayList<>();
            Thread.sleep(1);
        }

    }

    //-XX:CompileThreshold=1500 -XX:+PrintCompilation -XX:+CITime
    //JIT编译测试
    @Test
    public void jitTest() {
        for (int i = 0; i < 1400; i++) {
            testJIT();
        }
    }

    static long i = 0;
    private void testJIT() {
        i++;
    }


    static class STMThread extends Thread {
        HashMap map = new HashMap();

        @Override
        public void run() {
            try {
                while (true) {
                    //防止内存溢出
                    if (map.size() * 512/1024/1024 >= 400) {
                        map.clear();
                        System.out.println("clean map");
                    }
                    byte[] b1;
                    for (int i = 0; i < 100; i++) {
                        //模拟内存占用
                        b1 = new byte[512];
                        map.put(System.nanoTime(), b1);
                    }
                    Thread.sleep(1);
                }
            } catch (Exception ex) {

            }
        }
    }

    static class PrintThread extends Thread {
        public static final long startTime = System.currentTimeMillis();

        @Override
        public void run() {
            try {
                while (true) {
                    long t = System.currentTimeMillis() - startTime;
//                    System.out.println(t/1000 + "." + t%1000);
                    Thread.sleep(100);
                }
            } catch (Exception e) {

            }
        }
    }

}
