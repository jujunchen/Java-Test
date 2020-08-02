package design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/07/23
 */
public class JDKProxy implements InvocationHandler {

    private Object object;

    public JDKProxy(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        method.invoke(object, args);
        return null;
    }
}
