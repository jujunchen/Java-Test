import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/08/19
 */
public class InheritableThreadLocalTest {

    private static final InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<String> ();

    public static void main(String [] args) throws InterruptedException {
        threadLocal.set("hello world");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                threadLocal.set("son thread");
                System.out.println("thread:" + threadLocal.get());
            }
        });

        thread.start();

        thread.join();

        System.out.println("main:" + threadLocal.get());

        threadLocal.remove();
    }

}
