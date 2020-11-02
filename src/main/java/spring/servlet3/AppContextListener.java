package spring.servlet3;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenjujun
 * @date 2020-11-01
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 200, 50L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

        //将业务工作线程池放入servlet context对象
        servletContextEvent.getServletContext().setAttribute("executor", executor);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) servletContextEvent.getServletContext().getAttribute("executor");
        executor.shutdown();
    }
}
