package onJava.enums.multi;

import java.util.Random;

public class Ro {
    public  static <T extends Competitor<T>> void match(T a, T b) {
        System.out.println(a + " VS " + b + ":" + a.compete(b));
    }

    public static <T extends Enum<T> & Competitor<T>> void play(Class<T> rsbClass, int size) {
        for (int i = 0; i < size; i++) {
            //随机获取枚举值，并比较
            match(Enums.random(rsbClass), Enums.random(rsbClass));
        }
    }




}

/**
 * 枚举获取随机值
 */
class Enums {
    private static Random rand = new Random(47);
    public static
    <T extends Enum<T>> T random(Class<T> ec) {
        return random(ec.getEnumConstants());
    }
    public static <T> T random(T[] values) {
        return values[rand.nextInt(values.length)];
    }
}
