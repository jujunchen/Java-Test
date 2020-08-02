package design.observer.property;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * 被观察对象
 * @author jujun chen
 * @date 2020/07/26
 */
public class DeviceInfo {
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private int state;

    //增加监听
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    //移除监听
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public int getState() {
        return state;
    }

    public void setState(int newState) {
        int oldState = this.state;
        this.state = newState;
        changeSupport.firePropertyChange("state", oldState, state);
    }
}
