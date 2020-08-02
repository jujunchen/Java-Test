import lombok.SneakyThrows;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *  内存泄漏测试
 * @author jujun chen
 * @date 2020/07/02
 */
public class ThreadLocalTest {

    static class LocalVariable {
        private Long[] a = new Long[1024*1024];
    }

    final static Executor executors = Executors.newFixedThreadPool(5);

    final static ThreadLocal<LocalVariable> localVariable = new ThreadLocal<>();

    static Set<Thread> sets = new HashSet<>();

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 50; i++) {
            executors.execute(() -> {
                localVariable.set(new LocalVariable());
                System.out.println("use local varaible");
                sets.add(Thread.currentThread());
                localVariable.remove();
            });
            TimeUnit.SECONDS.sleep(1);
        }

        System.out.println("pool execute over");

        System.gc();

        System.out.println("gc over");
    }
}
