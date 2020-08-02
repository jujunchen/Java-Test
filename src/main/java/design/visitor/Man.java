package design.visitor;

/**
 * @author jujun chen
 * @date 2020/07/20
 */
public class Man extends Person {
    @Override
    public void accept(Action action) {
        action.getManResult(this);
    }
}
