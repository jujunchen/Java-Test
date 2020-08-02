import java.util.ArrayList;
import java.util.List;

/**
 * @author: jujun chen
 * @Type
 * @description: super extends 测试
 * @date: 2019/10/10
 */
public class CovariantArrays {

    public static void main(String[] args) {
        //上界，用于读
        List<? extends Fruit> flistTop = new ArrayList<>();
        //flistTop.add(new Fruit());
        //flistTop.add(new Apple());
        //flistTop.add(new Object());
        flistTop.add(null);
        Fruit fruit = flistTop.get(0);


        //下界，用于写
        List<? super Apple> flistBottem = new ArrayList<Apple>();
        flistBottem.add(new Apple());
        flistBottem.add(new Jonathan());
        //flistBottem.add(new Fruit());
        //get Apple对象会报错
        //Apple apple = flistBottem.get(0);
        Object object = flistBottem.get(0);
    }

}

class Fruit {

}

class Apple extends Fruit {

}

class Jonathan extends Apple {

}

class Orange extends Fruit {

}
