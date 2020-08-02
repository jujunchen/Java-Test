package design.filter;

/**
 * @author: jujun chen
 * @Type
 * @description: 过滤器测试
 * @date: 2019/07/23
 */
public class FilterTest {

    public static void main(String[] args) {
        String str = "中国好，共产党好，<黑帮>黑帮黑帮擦擦掉黑帮黑帮，<haha>";
        Request request = new Request();
        request.message = str;

        Response response = new Response();

        FilterChian filterChian = new FilterChian();
        filterChian.addFilter(new BlackWordFilter());
        filterChian.addFilter(new HtmlFilter());

        filterChian.doFilter(request, response);

        System.out.println(request.message);
        System.out.println(response.message);
    }
}
