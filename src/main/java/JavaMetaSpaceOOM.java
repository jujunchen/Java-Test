import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: jujun chen
 * @description: 使用了CGLIB来动态生成类，元空间存储类信息，-XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m
 * 如果只设置堆的大小，并不会溢出
 * @date: 2019/4/7
 */
public class JavaMetaSpaceOOM {

    static class OOMObject{}
    public static void main(final String[] args) {
        while (true){
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    return methodProxy.invokeSuper(o,objects);
                }
            });
            enhancer.create();
        }
    }

}
