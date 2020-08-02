package spi;

import java.util.ServiceLoader;

/**
 * @author: jujun chen
 * @Type
 * @description: Java SPI
 * @date: 2019/09/15
 */
public class SpiMain {

    public static void main(String[] args) {
        ServiceLoader<RemoveInterface> serviceLoader = ServiceLoader.load(RemoveInterface.class);

        if (serviceLoader != null) {
            for (RemoveInterface removeInterface : serviceLoader) {
                removeInterface.remove();
            }
        }
    }
}
