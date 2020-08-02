import org.junit.Test;

import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/08/26
 */
public class DelayQueueTest {

    @Test
    public void queueTest() throws InterruptedException {
        DelayQueue<Task> queue = new DelayQueue<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Task task = new Task(random.nextInt(1000), "taskName:" + i);
            queue.offer(task);
        }

        Task task;
        while ((task = queue.take()) != null) {
            System.out.println(task);
        }

    }
}

class Task implements Delayed {

    //延迟时间
    private final long delayTime;
    //到期时间
    private final long expire;
    //任务名称
    private String taskName;

    public Task(long delayTime, String taskName) {
        this.delayTime = delayTime;
        this.expire = System.currentTimeMillis() + delayTime;
        this.taskName = taskName;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(expire - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "Task{" +
                "delayTime=" + delayTime +
                ", expire=" + expire +
                ", taskName='" + taskName + '\'' +
                '}';
    }
}
