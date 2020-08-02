package design.visitor;

/**
 * 这里使用到了双分派，即首先在客户端程序中，将具体的状态作为参数传递到Woman中，第一个分派
 * 然后Woman类调用作为参数的"具体方法"getWomanResult，同时将自己传进去，第二次分派
 * @author jujun chen
 * @date 2020/07/20
 */
public class Woman extends Person {
    @Override
    public void accept(Action action) {
        action.getWomanResult(this);
    }
}
