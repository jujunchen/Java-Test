package design.proxy.newTest.dynamicProxy;

import design.proxy.newTest.staticProxy.DBQuery;
import design.proxy.newTest.staticProxy.IDBQuery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author jujun chen
 * @date 2020/07/25
 */
public class JdkDbQueryHandler implements InvocationHandler {

    IDBQuery real = null;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (real == null)
            real = new DBQuery();
        return real.request();
    }
}
