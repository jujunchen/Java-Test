package design.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jujun chen
 * @Type
 * @description: 关键词过滤
 * @date: 2019/07/23
 */
public class BlackWordFilter implements Filter {

    public static final List<String> words = new ArrayList<>();

    public BlackWordFilter() {
        words.add("黑帮");
        words.add("草");
    }

    @Override
    public void doFilter(Request request, Response response, FilterChian chian) {
        for (String word : words) {
            request.message = request.message.replaceAll(word, "**");
        }

        chian.doFilter(request, response);
        response.message += "-->" + this.getClass().getName();
    }
}
