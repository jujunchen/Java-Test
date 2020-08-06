
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author jujun chen
 * @date 2020/08/06
 */
public class MasterWorkerTest {

    public static void main(String[] args) {
        Master m = new Master(new PlusWorker(), 5);
        //提交100个子任务
        for (int i = 1; i <= 100; i++) {
            m.submit(i);
        }
        //开始计算
        m.execute();
        //最终计算结果保存在此
        int re = 0;
        Map<String, Object> resultMap = m.getResultMap();
        while (resultMap.size() > 0 || !m.isComplete()) {
            //不需要等待所有Worker都执行完，即可开始计算最终结果
            Set<String> keys = resultMap.keySet();
            String key = null;
            for (String k : keys) {
                key = k;
                break;
            }

            Integer i = null;
            if (key != null)
                i = (Integer) resultMap.get(key);
            if (i != null)
                re += i;
            if (key != null)
                resultMap.remove(key);
        }

        System.out.println(re);
    }
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

    //Master的构造，需要一个Worker进程逻辑，和需要的Worker数量
    public Master(Worker worker, int countWorker) {
        worker.setWorkQueue(workQueue);
        worker.setResultMap(resultMap);
        for (int i = 0; i < countWorker; i++) {
            threadMap.put(Integer.toString(i), new Thread(worker, Integer.toString(i)));
        }
    }

    //提交一个任务
    public void submit(Object job) {
        workQueue.add(job);
    }

    //返回子任务结果集
    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    //开始运行所有的worker进程，进行处理
    public void execute() {
        for (Map.Entry<String, Thread> entry : threadMap.entrySet()) {
            entry.getValue().start();
        }
    }
}

class Worker implements Runnable {
    //任务队列，用于取得子任务
    protected Queue<Object> workQueue;
    //子任务处理结果集
    protected Map<String,Object> resultMap;

    public void setWorkQueue(Queue<Object> workQueue) {
        this.workQueue = workQueue;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    //子任务处理的逻辑，在子类中实现具体逻辑
    public Object handle(Object input) {
        return input;
    }

    @Override
    public void run() {
        while (true) {
            //获取子任务
            Object input = workQueue.poll();
            if (input == null) break;
            //处理子任务
            Object re = handle(input);
            //将处理结果写入结果集
            resultMap.put(Integer.toString(input.hashCode()), re);
        }
    }
}

class PlusWorker extends Worker {
    @Override
    public Object handle(Object input) {
        Integer i = (Integer)input;
        return i*i*i;
    }
}

