package design.filter;

/**
 * @author: jujun chen
 * @Type
 * @description: 过滤器接口
 * @date: 2019/07/23
 */
public interface Filter {
    void doFilter(Request request, Response response, FilterChian chian);
}
