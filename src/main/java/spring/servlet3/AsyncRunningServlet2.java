package spring.servlet3;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 处理Servlet请求并开启异步,非阻塞I/O
 * @author chenjujun
 * @date 2020-11-01
 */
@WebServlet(urlPatterns = "/AsyncRunningServlet2", asyncSupported = true)
public class AsyncRunningServlet2 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(2 * 1000);

        ServletInputStream in = request.getInputStream();
        //异步读取，同时通过MyReadListener实现了非阻塞方式的获取
        in.setReadListener(new MyTestReadListener(in, asyncContext));

        //获取输出流，可直接返回响应内容给前端，异步处理可能还没有完成
        PrintWriter out = response.getWriter();
        out.println("");
        out.flush();
    }
}
