package design.observer.property;

/**
 * @author jujun chen
 * @date 2020/07/26
 */
public class DeviceTest {

    public static void main(String[] args) {
        DeviceInfo d = new DeviceInfo();
        d.addPropertyChangeListener(new DeviceChangeListener());
        d.setState(1);
    }
}
