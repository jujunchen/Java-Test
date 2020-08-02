package design.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/07/23
 */
public class FilterChian{

    private static final List<Filter> filters = new ArrayList<>();

    int index  = 0;

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public void doFilter(Request request, Response response) {
        if (index >= filters.size()) return;
        Filter filter = filters.get(index);
        index++;

        request.message += "\n--" + filter.getClass().getName();
        filter.doFilter(request, response, this);

    }
}
