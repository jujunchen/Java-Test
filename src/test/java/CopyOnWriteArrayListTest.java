import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/08/24
 */
public class CopyOnWriteArrayListTest {

    //模拟测试CopyOnWriteArrayList 的弱一致性
    @Test
    public void ListrayTest() throws InterruptedException {

        AryTest aryTest = new AryTest();

        StrClass strClass = new StrClass(aryTest.str1);
        String[] str2 = (String[]) strClass.getObjects();

        Thread thread = new Thread(() -> {
            aryTest.add();
            System.out.println(Arrays.toString(aryTest.str1));
        });

        thread.start();
        thread.join();

        System.out.println("str2=" + Arrays.toString(str2));
    }

    static class AryTest {
        String[] str1 = new String[]{"a", "b"};

        public void add() {
            String[] str2 = Arrays.copyOf(str1, str1.length + 1);
            str2[str2.length - 1] = "c";
            str1 = str2;
        }
    }

    static class StrClass {
        final Object[] objects;

        public StrClass(Object [] objects) {
            this.objects = objects;
        }

        public Object[] getObjects() {
            return objects;
        }
    }


    //copyOnWriteArrayList测试
    @Test
    public void copyOnWriteArrayList() throws InterruptedException {
        String[] str1 = new String[]{"a", "b"};
        List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>(str1);
        Thread thread = new Thread(() -> {
           copyOnWriteArrayList.add("c");
            System.out.println(copyOnWriteArrayList);
        });
        thread.start();
        System.out.println(copyOnWriteArrayList);
        thread.join();
    }

}
