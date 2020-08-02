/**
 * volatile测试原子性
 * @author jujun chen
 * @date 2020/07/03
 */
public class VolatileTest {

    public volatile int inc = 0;

    public synchronized void increase() {
        inc++;
    }

    public static void main(String[] args) {
        final VolatileTest test = new VolatileTest();

        for(int i=0; i < 10; i++) {
            new Thread(() -> {
                for(int j = 0; j < 1000; j++)
                    test.increase();
            }).start();
        }
        //保证前面的线程都执行完
        while (Thread.activeCount() > 1)
            Thread.yield();

        System.out.println(test.inc);
    }
}
