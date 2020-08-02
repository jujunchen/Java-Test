package produce_consumer;

import java.util.LinkedList;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/10/13
 */
public class Storage {

    private LinkedList<Object> linkedList = new LinkedList<>();

    private final int MAX_SIZE = 10;

    public void produce() throws InterruptedException {
        synchronized (linkedList) {
            while (linkedList.size() >= MAX_SIZE) {
                System.out.println("仓库已满,等待消费中..");
                linkedList.wait();
            }

            linkedList.add(new Object());
            System.out.println("生产一个对象");
            //生产一个，就让消费者消费一个
            linkedList.notifyAll();
        }
    }

    public void consume() throws InterruptedException {
        synchronized (linkedList) {
            while (linkedList.size() == 0) {
                System.out.println("仓库为空，等待生产中..");
                linkedList.wait();
            }
            linkedList.remove();
            System.out.println("消费一个对象");
            linkedList.notifyAll();
        }
    }

}
