package design.visitor;

/**
 * @author jujun chen
 * @date 2020/07/20
 */
public abstract class Person {

    //提供一个方法，让访问者可以访问
    public abstract void accept(Action action);
}
