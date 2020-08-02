package design.proxy;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/07/23
 */
public class Person implements Animal {
    @Override
    public void eat() {
        System.out.println("吃苹果");
    }
}
