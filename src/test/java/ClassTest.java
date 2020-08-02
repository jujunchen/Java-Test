import drools.Person;
import junit.framework.TestSuite;
import org.junit.Test;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/06/24
 */
public class ClassTest extends TestSuite {

    @Test
    public void componentType() throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        String genericClassName = this.getClass().toGenericString();
        String className = this.getClass().toString();
        System.out.println(genericClassName);
        System.out.println(className);
        System.out.println("---------");

        Class cla = Class.forName("drools.Person");
        System.out.println(cla);
        Person person = (Person) cla.newInstance();
        person.setName("小王");
        System.out.println(person);
        Type type = cla.getGenericSuperclass();
        System.out.println(type.getTypeName());
        System.out.println(cla.getPackage());
        System.out.println(cla.getSigners());
        System.out.println(cla.getEnclosingMethod());
        System.out.println(Arrays.toString(cla.getDeclaredClasses()));
        System.out.println(Arrays.toString(cla.getDeclaredMethods()));
        System.out.println(Arrays.toString(cla.getAnnotations()));
        System.out.println("---------");


        String[] strAry = new String[]{"1", "2"};
        System.out.println(strAry.getClass().getComponentType());
    }


}
