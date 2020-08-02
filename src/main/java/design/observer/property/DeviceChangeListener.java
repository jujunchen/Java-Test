package design.observer.property;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * 观察者
 * @author jujun chen
 * @date 2020/07/26
 */
public class DeviceChangeListener implements PropertyChangeListener {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt);
    }
}
