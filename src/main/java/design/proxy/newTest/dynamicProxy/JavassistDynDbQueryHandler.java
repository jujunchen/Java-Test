package design.proxy.newTest.dynamicProxy;

import design.proxy.newTest.staticProxy.DBQuery;
import design.proxy.newTest.staticProxy.IDBQuery;
import javassist.util.proxy.MethodHandler;

import java.lang.reflect.Method;

/**
 * @author jujun chen
 * @date 2020/07/25
 */
public class JavassistDynDbQueryHandler implements MethodHandler {

    IDBQuery real = null;

    @Override
    public Object invoke(Object o, Method method, Method method1, Object[] objects) throws Throwable {
        if (real == null)
            real = new DBQuery();
        return real.request();
    }
}
