package design.proxy.newTest.dynamicProxy;

import design.proxy.newTest.staticProxy.DBQuery;
import design.proxy.newTest.staticProxy.IDBQuery;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author jujun chen
 * @date 2020/07/25
 */
public class CglibDbQueryInterceptor implements MethodInterceptor {

    IDBQuery real = null;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (real == null)
            real = new DBQuery();
        return real.request();
    }
}
