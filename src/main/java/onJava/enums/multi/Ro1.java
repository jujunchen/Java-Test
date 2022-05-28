package onJava.enums.multi;

import java.util.Random;

/**
 * 测试类
 */
public class Ro1 {

    private static Random random = new Random(47);

    static final  int SIZE = 20;

    /**
     * 随机生产
     * @return
     */
    public static Item newItem() {
        switch (random.nextInt(3)) {
            default:
            case 0: return new Scissors();
            case 1: return new Paper();
            case 2: return new Rock();
        }
    }

    /**
     * 两个参数比较
     * @param a
     * @param b
     */
    public static void match(Item a, Item b) {
        System.out.println(a + " VS " + b + ":" + a.compete(b));
    }

    public static void main(String[] args) {
        //随机生成20次记录
        for (int i = 0; i < SIZE; i++) {
            match(newItem(), newItem());
        }
    }
}
