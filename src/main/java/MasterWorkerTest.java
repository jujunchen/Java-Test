import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author jujun chen
 * @date 2020/08/06
 */
public class MasterWorkerTest {

}

final class Master {
    //任务队列
    protected Queue<Object> workQueue = new ConcurrentLinkedQueue<>();

    //Worker进程队列
    protected Map<String,Thread> threadMap = new HashMap<>();

    //子任务处理结果集
    protected Map<String,Object> resultMap = new ConcurrentHashMap<>();

    //是否所有的子任务都结束了
    public boolean isComplete() {
        for (Map.Entry<String, Thread> entry : threadMap.entrySet()) {
            if (entry.getValue().getState() != Thread.State.TERMINATED) {
                return false;
            }
        }
        return true;
    }
}
