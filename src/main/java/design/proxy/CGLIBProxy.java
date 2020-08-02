package design.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/10
 */
public class CGLIBProxy {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Person.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                System.err.println("Before invoke " + method);
                //可以不返回值
                Object result = proxy.invokeSuper(obj, args);
                System.err.println("After invoke " + method);
                return result;
            }
        });

        Person proxy = (Person) enhancer.create();
        proxy.eat();
    }
}
