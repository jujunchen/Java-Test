package produce_consumer;

/**
 * @author: jujun chen
 * @Type
 * @description: 生产者/消费者模式
 * @date: 2019/10/13
 */
public class MainDemo {

    public static void main(String[] args) {
        Storage storage = new Storage();

        Thread produceThread = new Thread(new Producer(storage));
        Thread consumeThread = new Thread(new Consumer(storage));

        produceThread.start();
        consumeThread.start();
    }


}
