package zookeeper.distributeLock;
import	java.util.Random;
import	java.util.concurrent.CountDownLatch;
import	java.util.TreeSet;
import	java.util.SortedSet;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: jujun chen
 * @Type
 * @description: java api实现方式
 * @date: 2019/09/29
 */
public class ZookeeperApiLock {

    public static final String LOCK = "/LOCKS";

    private ZooKeeper zooKeeper;

    private int sessionTimeount;

    private String locakID;

    private final static byte[] data = {1,2};

    private final CountDownLatch count = new CountDownLatch(1);

    public ZookeeperApiLock() {
        this.zooKeeper = ZookeeperClient.getInstance();
        this.sessionTimeount = ZookeeperClient.getSessionTimeout();

    }

    public  boolean lock () throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.exists(LOCK, null);
        if (stat == null) {
            zooKeeper.create(LOCK, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        locakID = zooKeeper.create(LOCK + "/", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.printf("%s成功创建lock节点[%s]，开始竞争锁\n", Thread.currentThread().getName(), locakID);

        List<String> childrenNodes = zooKeeper.getChildren(LOCK, true);

        SortedSet<String> sortedSet = new TreeSet<String> ();

        for (String childrenNode : childrenNodes) {
            sortedSet.add(LOCK + "/" + childrenNode);
        }
        String first = sortedSet.first();
        if (locakID.equals(first)) {
            System.out.println(Thread.currentThread().getName() + "->成功的锁,lock节点为" + locakID);
            return true;
        }

        SortedSet<String> lessThanLockId = sortedSet.headSet(locakID);

        if (!lessThanLockId.isEmpty()) {
            String prevLockId = lessThanLockId.last();
            zooKeeper.exists(prevLockId, new LockWatcher(prevLockId,count));
            count.await(sessionTimeount, TimeUnit.SECONDS);

            System.out.println(Thread.currentThread().getName() + "->成功获取锁：" + locakID);
            return true;
        }
        return false;
    }


    public  boolean unlock() {
        System.out.println(Thread.currentThread().getName() + "->开始释放锁" + locakID);
        try {
            zooKeeper.delete(locakID, -1);
            System.out.println(Thread.currentThread().getName() + "->节点" + locakID + "成功被删除");
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(10);

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                ZookeeperApiLock lock = null;
                try {
                    lock = new ZookeeperApiLock();
                    latch.countDown();
                    latch.await();
                    lock.lock();
                    Thread.sleep(random.nextInt(500));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (lock != null) {
                        lock.unlock();
                    }
                }

            }).start();
        }
    }


 }
