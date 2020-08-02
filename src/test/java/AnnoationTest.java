import junit.framework.TestSuite;
import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @Author: jujun chen
 * @Description:
 * @Date: 2017/12/27
 */
public class AnnoationTest extends TestSuite {

    public void tools(){
        Class clazz = ForumService.class;

        Method[] methods = clazz.getMethods();

        for(Method method : methods){

            NeedTest nt = method.getAnnotation(NeedTest.class);

            if (nt != null){

            }

        }

    }

    @Test
    public void modifierTest(){
        Class clazz = A.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods){
            int modifier = method.getModifiers();

            if (Modifier.isPublic(modifier)){
                System.out.println(" " + method.getName());
            }
        }
    }



}

class A{
    public  void a1(){

    }

    public  void a2(){

    }

    protected void  a3(){

    }

    protected  void a4(){}

}
