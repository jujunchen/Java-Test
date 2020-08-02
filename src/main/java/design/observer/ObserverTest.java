package design.observer;

/**
 * @author jujun chen
 * @date 2020/07/26
 */
public class ObserverTest {

    public static void main(String[] args) {
        ConcreteSubject subject = new ConcreteSubject();
        subject.attach(new ConcreteObserver());

        //改变被观察者的状态
        subject.setState(1);

    }
}
