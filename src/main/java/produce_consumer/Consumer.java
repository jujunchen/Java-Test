package produce_consumer;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/10/13
 */
public class Consumer implements Runnable {

    private Storage storage;

    public Consumer(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        while (true) {
            try {
               // Thread.sleep(1000);
                storage.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
