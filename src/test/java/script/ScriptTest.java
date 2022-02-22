package script;

import junit.framework.TestSuite;
import org.junit.Test;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import javax.enterprise.inject.New;
import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;
import java.io.File;
import java.io.FileReader;
import java.util.Date;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/12/07
 */
public class ScriptTest extends TestSuite {


    /**
     * 从文件中读取Js脚本
     * var obj = new Object();
     * obj.hello = function (name) {
     *     print('Hello, ' + name);
     * }
     * @throws Exception
     */
    @Test
    public void file() throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        engine.eval(new FileReader(new File("/Users/chenjujun/java-projects/Java-Test/src/test/java/script/test.js")));
        Invocable inv = (Invocable) engine;
        Object obj = engine.get("obj");
        inv.invokeMethod(obj, "hello", "Script Test!" );
        inv.invokeMethod(obj, "hello2", "xxxx");
    }

    @Test
    public void scriptVar() throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        File file = new File("F:/test/test.txt");
        //将File对象f直接注入到js脚本中并可以作为全局变量使用
        engine.put("files", file);
        engine.eval("print(files.getPath());print(files.getName());print(files.isFile)");
    }


    @Test
    public void scriptTest1() throws ScriptException, NoSuchMethodException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("JavaScript");

        StringBuilder sb = new StringBuilder();
        sb.append("var obj = new Object();");
        sb.append("obj.hello = function(name){print('Hello, ' + name);}");
        engine.eval(sb.toString());

        //Invocable 可以调用已经加载过的脚本
        Invocable invocable = (Invocable) engine;
        //获取脚本的obj对象
        Object object = engine.get("obj");
        //调用obj对象的hello函数
        invocable.invokeMethod(object, "hello", "Script Method!");
    }


    @Test
    public void scriptTest() throws ScriptException {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("JavaScript");
        engine.put("x", "Hello World!");
        engine.eval("print(x)");

        //全局变量
        engineManager.put("x", "xxxx");
        engine.eval("print(x)", engineManager.getBindings());



        ScriptContext context = new SimpleScriptContext();
        //新的Script context绑定ScriptContext的ENGINE_SCOPE
        Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);

        // 增加一个新变量到新的范围 engineScope 中
        bindings.put("x", "word hello!!");
        // 执行同一个脚本 - 但这次传入一个不同的script context
        engine.eval("print(x);", bindings);
        engine.eval("print(x);");
    }

    @Test
    public void runnableImpl() throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        // String里定义一段JavaScript代码脚本
        String script = "function run() { print('run called'); }";
        // 执行这个脚本
        engine.eval(script);

        // 从脚本引擎中获取Runnable接口对象（实例）. 该接口方法由具有相匹配名称的脚本函数实现。
        Invocable inv = (Invocable) engine;
        // 在上面的脚本中，我们已经实现了Runnable接口的run()方法
        Runnable runnable = inv.getInterface(Runnable.class);

        // 启动一个线程运行上面的实现了runnable接口的script脚本
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Test
    public void runnableObject() throws ScriptException, InterruptedException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        String script = "var obj = new Object();obj.run = function() {print('run method called');}";
        engine.eval(script);

        //获得脚本对象
        Object object = engine.get("obj");
        Invocable invocable = (Invocable) engine;
        Runnable runnable = invocable.getInterface(object, Runnable.class);

        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(1000);
    }
    
    /**
     * groovy脚本使用
     * @throws ScriptException 
     * @throws NoSuchMethodException 
     */
    
    ScriptEngineManager manager = new ScriptEngineManager();
	ScriptEngine engine = manager.getEngineByName("groovy");
	
    @Test
	public void groovyTest() throws ScriptException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    	Bindings bindings = engine.createBindings();
    	bindings.put("date", new Date());
    	engine.eval("def getTime(){return date.getTime();}", bindings);
    	engine.eval("def sayHello(name, age) {return 'Hello,' + name + ',' + age;}");
    	Invocable invocable = (Invocable) engine;
    	Long time = (Long) invocable.invokeFunction("getTime", null);
    	System.out.println(time);
    	String message = (String) invocable.invokeFunction("sayHello", "zhangsan", 12);
    	System.out.println(message);
    	//
    	Bindings bindings2 = engine.createBindings();
    	bindings2.put("date", new Date());
    	engine.eval("def getTime(){return date.getSeconds();}", bindings2);
    	Integer time2 = (Integer) invocable.invokeFunction("getTime", null);
    	System.out.println(time2);
    	
    	//方法被覆盖
//    	Long time3 = (Long) invocable.invokeFunction("getTime", null);
//    	System.out.println(time3);
    	
    	engine.eval("def sayHello(name) {return 'Hello,' + getName(name);}");
    	engine.eval("def getName(name){return '----' + name;}");
    	String message2 = (String) invocable.invokeFunction("sayHello", "zhangsan");
    	System.out.println(message2);
    	
//    	String helloString = "class Hello{ def sayHello(name) {return 'Hello,' + name;} def getName(){return 'xxx';}}";
//    	
//    	GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
////    	Class helloClass = groovyClassLoader.parseClass("def sayHello(name) {return 'Hello,' + getName(name);} def getName(name){return '----' + name;}");
//    	Class helloClass = groovyClassLoader.parseClass(helloString);
//    	GroovyObject object = (GroovyObject) helloClass.newInstance();
//    	Object retObject = object.invokeMethod("sayHello", "xxx");
//    	System.out.println(retObject.toString());
	}
    
    @Test
    public void groovyFile() throws Exception{
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("groovy");
        //不同方法间换行
        String script = "def hello() {return \"xxx\";}\n def hello2() {return \"xxx2\";}";
//        engine.eval(new FileReader(new File("/Users/chenjujun/java-projects/Java-Test/src/test/java/script/test.groovy")));
        engine.eval(script);
        Invocable inv = (Invocable) engine;
        String msg = (String) inv.invokeFunction("hello", null);
        String msg2 = (String) inv.invokeFunction("hello2", null);
        System.out.println(msg);
        System.out.println(msg2);
    }


}
