package spring.servlet3;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 异步监听
 * @author chenjujun
 * @date 2020-11-01
 */
@WebListener
public class MyAsyncListener implements AsyncListener {

    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        System.out.println("MyAsyncListener onComplete.");
    }

    //当处理超时的时候，就会调用该方法
    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        System.out.println("MyAsyncListener onTimeout.");
        ServletResponse response = event.getAsyncContext().getResponse();
        PrintWriter out = response.getWriter();
        out.write("Timeout 处理超时.");
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        System.out.println("MyAsyncListener onError.");
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        System.out.println("MyAsyncListener onStartAsync.");
    }
}
