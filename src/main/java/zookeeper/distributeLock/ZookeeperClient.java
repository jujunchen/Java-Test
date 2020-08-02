package zookeeper.distributeLock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/29
 */
public class ZookeeperClient {

    private static final String CONNECT = "127.0.0.1";

    private static int sessionTimeout = 5000;

    private static final ZooKeeper zk;

    static {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper(CONNECT, sessionTimeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        zk = zooKeeper;
    }

    public static ZooKeeper getInstance() {
        return zk;
    }

    public static int getSessionTimeout() {
        return sessionTimeout;
    }
}
