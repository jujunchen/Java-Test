import java.util.ArrayList;
import java.util.List;

public class ExtendsAndSuperTest {

    public static void main(String[] args)

    {
        /**
         * 这个通配符表示“某个未知的子类型，它是Fruit或其子类”。当你看到这样的类型，你可以确定的是这个未知的类型至少是一个Fruit。但是，你不能确定它是哪个具体的子类型。
         *
         * 使用场景：当你希望从一个集合中取出元素，并且你知道这些元素至少是Fruit类型时，你可以使用<? extends Fruit>。
         *
         * 限制：由于编译器不知道这个未知的具体类型是什么，你不能向这个集合中添加元素（除了null），因为添加的元素可能会违反集合的类型安全。
         */
        //测试extends
        List<? extends Fruit> list = new ArrayList<>();
        //不能增加
//        list.add(new Apple("apple"));
        //也不能增加
//        list.add(new Fruit("fruit"));
        //可以获取，至少知道是Fruit类型
        Fruit fruit = list.get(0);


        /**
         * 这个通配符表示“某个未知的父类型，它是Apple的父类或Apple本身”。当你看到这样的类型，你知道的是这个未知的类型至少是Apple的父类，但不一定是Fruit（除非没有其他类在Apple和Fruit之间）。
         *
         * 使用场景：当你希望向一个集合中添加元素，并且你知道这些元素是Apple或其父类类型时，你可以使用<? super Apple>。此外，你还可以从这个集合中安全地取出Object类型的元素（因为所有的类都是Object的子类）。
         *
         * 限制：由于这个集合可以包含Apple的任意父类，所以从集合中取出的元素通常会被视为Object类型，除非你进行了类型转换。
         */

        List<? super Apple> list2 = new ArrayList<>();
        list2.add(new Apple("apple"));
        //不能增加，因为不确定是哪个父类
//        list2.add(new Fruit("fruit"));
        Object obj = list2.get(0);
    }

    static class Fruit
    {
        String name;

        public Fruit(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }

    static class Apple extends Fruit {
        public Apple(String name)
        {
            super(name);
        }
    }
}
