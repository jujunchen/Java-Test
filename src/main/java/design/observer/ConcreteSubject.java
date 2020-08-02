package design.observer;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 被观察者
 * @author jujun chen
 * @date 2020/07/26
 */
public class ConcreteSubject implements ISubject{

    CopyOnWriteArrayList<IObserver> observers = new CopyOnWriteArrayList<>();

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        inform();
    }

    @Override
    public void attach(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void inform() {
        for (IObserver observer : observers) {
            observer.update();
        }
    }
}
