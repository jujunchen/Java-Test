import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import	java.util.concurrent.CountDownLatch;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author: jujun chen
 * @Type
 * @description: zk测试
 * @date: 2019/09/27
 */
public class ZkCreateDemo {

    public static final CountDownLatch count = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 1000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    count.countDown();
                    System.out.printf("%s\n", watchedEvent.getState());
                }
            }
        });

        count.await();
        System.out.printf("zookeeper: %s\n", zooKeeper.getState());


        String result = zooKeeper.create("/demo","3333".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.printf("创建节点%s\n", result);


    }

}
