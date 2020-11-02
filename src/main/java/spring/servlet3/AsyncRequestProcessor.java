package spring.servlet3;

import javax.servlet.AsyncContext;
import java.io.PrintWriter;

/**
 * 业务工作线程
 * @author chenjujun
 * @date 2020-11-01
 */
public class AsyncRequestProcessor implements Runnable {

    private AsyncContext asyncContext;

    private int time;

    public AsyncRequestProcessor(AsyncContext asyncContext, int time) {
        this.asyncContext = asyncContext;
        this.time = time;
    }

    @Override
    public void run() {
        System.out.println("是否异步：" + asyncContext.getRequest().isAsyncSupported());
        longProcessing(time);

        try {
            PrintWriter out = asyncContext.getResponse().getWriter();
            out.write("处理完成 " + time + "ms!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //完成处理
        asyncContext.complete();
    }

    private void longProcessing(int time) {
        //模拟业务处理时间
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
