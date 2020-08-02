package design.observer;

/**
 * 具体观察者
 * @author jujun chen
 * @date 2020/07/26
 */
public class ConcreteObserver implements IObserver {

    @Override
    public void update() {
        System.out.println("observer receives information");
    }
}
