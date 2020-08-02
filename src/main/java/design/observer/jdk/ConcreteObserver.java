package design.observer.jdk;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者，实现Observer接口
 * @author jujun chen
 * @date 2020/07/26
 */
public class ConcreteObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ConcreteSubject) {
            System.out.println("observer recevice info: " + arg.toString());
        }
    }
}
