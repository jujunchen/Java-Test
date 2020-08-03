import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jujun chen
 * @date 2020/08/03
 */
public class PooableObjectFactoryDemo implements PooledObjectFactory {

    private static AtomicInteger counter = new AtomicInteger(0);

    @Override
    public PooledObject makeObject() throws Exception {
        Object object = String.valueOf(counter.getAndIncrement());
        System.out.println("Create Object " + object);
        return (PooledObject) object;
    }

    @Override
    public void destroyObject(PooledObject p) throws Exception {
        System.out.println("Destorying Object " + p);
    }

    @Override
    public boolean validateObject(PooledObject p) {
        return true;
    }

    @Override
    public void activateObject(PooledObject p) throws Exception {
        System.out.println("Before borrow " + p);
    }

    @Override
    public void passivateObject(PooledObject p) throws Exception {
        System.out.println("return " + p);
    }
}
