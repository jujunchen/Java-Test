package hashmap;

import lombok.SneakyThrows;
import spring.Person;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author chenjujun
 * @date 2021/7/7
 */
public class WeakHashMapTest {

    @SneakyThrows
    public static void main(String[] args) {
        WeakHashMap<Person, Person> weakHashMap = new WeakHashMap<>();
        Person person = new Person();
        person.name="123";
        Person person1 = new Person();
        weakHashMap.put(person1, person);
        while (true) {
            System.gc();
            System.out.println(weakHashMap.get(person1).name);
            TimeUnit.SECONDS.sleep(10);
        }


    }
}
