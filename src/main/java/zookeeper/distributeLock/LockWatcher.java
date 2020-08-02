package zookeeper.distributeLock;
import	java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.context.annotation.Lazy;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/29
 */
public class LockWatcher implements Watcher {

    private CountDownLatch latch;

    private String prevLockId;

    public LockWatcher(String prevLockId, CountDownLatch count) {
        this.prevLockId = prevLockId;
        this.latch = count;
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDeleted) {
            latch.countDown();
        }
    }
}
