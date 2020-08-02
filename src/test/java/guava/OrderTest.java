package guava;

import com.google.common.collect.Ordering;
import junit.framework.TestSuite;
import lombok.Data;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/07/25
 */
public class OrderTest  extends TestSuite {

    @Test
    public void orderTest() {
        int a = 10;
        int b = 20;
        System.out.println(Ordering.natural().min(a, b));;

        Person person = new Person("张三", 18);
        Person person1 = new Person("老李", 49);

        System.out.println(Ordering.natural().min(person, person1));

        Person minPerson = Collections.min(Arrays.asList(person, person1));
        System.out.println(minPerson);

    }

    @Data
    class Person implements Comparable{
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof Person) {
                Person that = (Person) o;
                return this.age - that.age;
            }
            return -1;
        }
    }
}
