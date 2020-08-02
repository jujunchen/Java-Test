package design.observer.jdk;

/**
 * 使用JDK自带观察者
 * @author jujun chen
 * @date 2020/07/26
 */
public class ObserverTest {

    public static void main(String[] args) {
        ConcreteSubject subject = new ConcreteSubject();
        //通知顺序跟注册顺序相反
        subject.addObserver(new ConcreteObserver());
        subject.addObserver(new ConcreteObserver2());
        subject.changeState(1);
    }
}
