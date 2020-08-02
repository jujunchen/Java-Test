package design.filter;

/**
 * @author: jujun chen
 * @Type
 * @description: html过滤器
 * @date: 2019/07/23
 */
public class HtmlFilter implements Filter {

    @Override
    public void doFilter(Request request, Response response, FilterChian chian) {
        request.message = request.message
                .replace("<" , "[")
                .replace(">", "]");
        chian.doFilter(request, response);
        response.message += "-->" + this.getClass().getName();
    }
}
