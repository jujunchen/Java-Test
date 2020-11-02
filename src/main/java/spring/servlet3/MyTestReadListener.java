package spring.servlet3;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 异步处理I/O
 * @author chenjujun
 * @date 2020-11-01
 */
public class MyTestReadListener implements ReadListener {
    private ServletInputStream inputStream;

    private AsyncContext asyncContext;

    public MyTestReadListener(ServletInputStream in, AsyncContext asyncContext) {
        this.inputStream = in;
        this.asyncContext = asyncContext;
    }

    //当inputstream流中的数据达到可读时，触发调用方法
    @Override
    public void onDataAvailable() throws IOException {
        System.out.println("执行到我了,数据可用");
    }

    //当inputstream 流中的数据读完时，调用该方法，并在该方法内部完成业务逻辑处理
    @Override
    public void onAllDataRead() throws IOException {
        try {
            //模拟耗时的业务处理
            Thread.sleep(1000);
            PrintWriter out = asyncContext.getResponse().getWriter();
            out.write("完成业务处理");
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("出错了");
        t.printStackTrace();
    }
}
