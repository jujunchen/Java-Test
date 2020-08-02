import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 从文件流中获取单例，测试
 * @author jujun chen
 * @date 2020/07/24
 */
public class SingletonFormStreamTest {

    @SneakyThrows
    @Test
    public void test() {
        SerSingleton s1 = null;
        SerSingleton s = SerSingleton.getInstance();
        //先将实例串行化到文件
        FileOutputStream fos = new FileOutputStream("SerSingleton.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(s);
        oos.flush();
        oos.close();

        //从文件读出原有的单例类
        FileInputStream fis = new FileInputStream("SerSingleton.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        s1 = (SerSingleton) ois.readObject();
        Assert.assertEquals(s, s1);
    }
}

class SerSingleton implements Serializable {
    String name;

    private SerSingleton() {
        System.out.println("Singleton is create");
        name = "SerSingleton";
    }

    private static SerSingleton instance = new SerSingleton();
    public static SerSingleton getInstance() {
        return instance;
    }

    public static void createString() {
        System.out.println("createString in Singleton");
    }

    //阻止生成新的实例，总是返回当前对象
    //如果去掉这段，来回导致生成两个对象
    private Object readResolve() {
        return instance;
    }
}
