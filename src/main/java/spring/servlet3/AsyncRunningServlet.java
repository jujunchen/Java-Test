package spring.servlet3;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 处理Servlet请求并开启异步
 * @author chenjujun
 * @date 2020-11-01
 */
@WebServlet(urlPatterns = "/AsyncRunningServlet", asyncSupported = true)
public class AsyncRunningServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        System.out.println("AsyncRunningServlet Start | Name=" + Thread.currentThread().getName() + " | ID=" + Thread.currentThread().getId());

        request.setAttribute("org.apache.catalina.ASYNC_SUPPORT", true);

        //动态设置模拟后续业务逻辑处理的时间，便于测试对比异步上下文时间超时与不超时的代码反应
        String time = request.getParameter("time");
        int processTime = Integer.valueOf(time);
        //最大设置5s
        if (processTime > 5000) {
            processTime = 5000;
        }

        AsyncContext asyncContext = request.startAsync();
        //添加监听MyAsyncListener
        asyncContext.addListener(new MyAsyncListener());
        //异步Servlet处理有对应的超时时间，如果在指定的时间内没有处理完业务逻辑，则会抛出异常
        asyncContext.setTimeout(4000);


        //获取业务工作线程池，这里的executor是在上下文AppContextListener类中设置的
        ThreadPoolExecutor executor = (ThreadPoolExecutor) request.getServletContext().getAttribute("executor");

        //异步执行具体的业务逻辑，同时把processTime模拟业务逻辑处理的时间传进去
        executor.execute(new AsyncRequestProcessor(asyncContext, processTime));

        //通过打印线程ID,可以发现当前线程没有变化
        long endTime = System.currentTimeMillis();
        System.out.println("AsyncRunningServlet End | Name=" + Thread.currentThread().getName()
                + " | ID=" + Thread.currentThread().getId()
                + " | Time Cost=" + (endTime - startTime) + " ms.");
    }
}
