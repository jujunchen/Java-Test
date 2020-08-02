package design.proxy.newTest.dynamicProxy;

import design.proxy.newTest.staticProxy.DBQuery;
import design.proxy.newTest.staticProxy.IDBQuery;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * @author jujun chen
 * @date 2020/07/25
 */
public class DynamicProxyTest {

    //创建JDK动态代理
    public static IDBQuery createJdkProxy() {
        IDBQuery jdkProxy = (IDBQuery) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[]{IDBQuery.class}, new JdkDbQueryHandler());
        return jdkProxy;
    }

    //cglib创建动态代理
    public static IDBQuery createCglibProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new CglibDbQueryInterceptor());
        enhancer.setInterfaces(new Class[]{IDBQuery.class});
        IDBQuery cglibProxy = (IDBQuery) enhancer.create();
        return cglibProxy;
    }

    //Javassist创建动态代理,工厂方式创建
    public static IDBQuery createJavassistDynProxy() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ProxyFactory proxyFactory = new ProxyFactory();
        //指定接口
        proxyFactory.setInterfaces(new Class[]{IDBQuery.class});
        Class proxyClass = proxyFactory.createClass();
        IDBQuery javassistProxy = (IDBQuery) proxyClass.getDeclaredConstructor().newInstance();
        //设置Handler处理器
        ((ProxyObject)javassistProxy).setHandler(new JavassistDynDbQueryHandler());
        return javassistProxy;
    }

    //Javassist使用动态Java代码创建
    public static IDBQuery createJavassistBytecodeDynamicProxy() throws NotFoundException, CannotCompileException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ClassPool mPool = new ClassPool(true);
        //定义类名
        CtClass mCtc = mPool.makeClass(IDBQuery.class.getName() + "JavassistBytecodeProxy");
        //需要实现的接口
        mCtc.addInterface(mPool.get(IDBQuery.class.getName()));
        //添加构造函数
        mCtc.addConstructor(CtNewConstructor.defaultConstructor(mCtc));
        //添加类的字段信息，使用动态java代码
        mCtc.addField(CtField.make("public " + IDBQuery.class.getName() + " real;", mCtc));
        String dbqueryname = DBQuery.class.getName();
        //添加方法，这里使用动态Java代码指定内部逻辑
        mCtc.addMethod(CtNewMethod.make("public String request() {if(real==null)real=new " + dbqueryname + "();return real.request();}", mCtc));
        //基于以上信息，生成动态类
        Class pc = mCtc.toClass();
        //生成动态类的实例
        IDBQuery bytecodeProxy = (IDBQuery) pc.getDeclaredConstructor().newInstance();
        return bytecodeProxy;
    }

    //几种创建方法性能比较
    //创建时间：CGLIB > Javassist动态代码创建 > Javassist工厂类创建 > Jdk
    //调用时间：Javassist工厂类创建 > Jdk > CGLIB > Javassist动态代码创建
    public static final int CIRCLE = 3000000;
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, NotFoundException, CannotCompileException {
        IDBQuery d = null;
        long begin = System.currentTimeMillis();
        d = createJdkProxy(); //测试JDK动态代理
        System.out.println("createJdkProxy:" + (System.currentTimeMillis() - begin));
        System.out.println("JdkProxy class:" + d.getClass().getName());
        begin = System.currentTimeMillis();
        for (int i = 0; i < CIRCLE; i++) {
            d.request();
        }
        System.out.println("callJdkProxy:" + (System.currentTimeMillis() - begin));

        //测试CGLIB动态代理
        begin = System.currentTimeMillis();
        d = createCglibProxy();
        System.out.println("createCGlibProxy:" + (System.currentTimeMillis() - begin));
        System.out.println("CglibProxy class:" + d.getClass().getName());
        begin=System.currentTimeMillis();
        for (int i = 0; i < CIRCLE; i++) {
            d.request();
        }
        System.out.println("callCglibProxy:" + (System.currentTimeMillis()-begin));


        //测试Javassist动态代理
        begin = System.currentTimeMillis();
        d = createJavassistDynProxy();
        System.out.println("createJavassistDynProxy:" + (System.currentTimeMillis() - begin));
        System.out.println("JavassistDynProxy class:" + d.getClass().getName());
        begin = System.currentTimeMillis();
        for (int i = 0; i < CIRCLE; i++) {
            d.request();
        }
        System.out.println("callJavassistDynProxy:" + (System.currentTimeMillis() - begin));

        //测试Javassist动态java代码创建
        begin = System.currentTimeMillis();
        d = createJavassistBytecodeDynamicProxy();
        System.out.println("createJavassistBytecodeDynamicProxy:" + (System.currentTimeMillis() - begin));
        System.out.println("JavassistBytecodeDynamicProxy class:" + d.getClass().getName());
        begin = System.currentTimeMillis();
        for (int i = 0; i < CIRCLE; i++) {
            d.request();
        }
        System.out.println("callJavassistBytecodeDynamicProxy:" + (System.currentTimeMillis() - begin));
    }

}
