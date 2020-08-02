package design.observer.jdk;

import java.util.Observable;

/**
 * 被观察者，继承Observable
 * @author jujun chen
 * @date 2020/07/26
 */
public class ConcreteSubject extends Observable {

    public void changeState(int state) {
        this.setChanged();
        this.notifyObservers(state);
    }
}
