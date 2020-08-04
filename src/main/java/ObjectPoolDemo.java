import lombok.SneakyThrows;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jujun chen
 * @date 2020/08/03
 */
public class ObjectPoolDemo {
    static PooledObjectFactory factory = new PooableObjectFactoryDemo();
    static ObjectPool pool = new GenericObjectPool(factory);
    private static AtomicInteger endcount = new AtomicInteger(0);

    public static class PoolThread extends Thread {
        @SneakyThrows
        @Override
        public void run() {
            Object obj = null;
            try {
                for (int i = 0; i < 100; i++) {
                    System.out.println("== " + i + " ==");
                    obj = pool.borrowObject();
                    System.out.println(obj = " is get");

                    pool.returnObject(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                endcount.getAndIncrement();
            }
        }
    }

    public static void main(String[] args) {
        new PoolThread().start();
        new PoolThread().start();
        new PoolThread().start();
        try {
            while (true) {
                if (endcount.get() == 3) {
                    pool.close();
                    break;
                }
            }
        } catch (Exception e) {

        }
    }
}
