package design.proxy;

import com.google.common.reflect.Reflection;

import java.lang.reflect.Proxy;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/07/23
 */
public class ProxyTest {
    public static void main(String[] args) {
        Person person = new Person();
        JDKProxy jdkProxy = new JDKProxy(person);
        Animal proxy = (Animal) Proxy.newProxyInstance(Animal.class.getClassLoader(), new Class[]{Animal.class}, jdkProxy);
/*
        //guava
        Animal proxy = Reflection.newProxy(Animal.class, jdkProxy);
*/
        proxy.eat();
    }
}
