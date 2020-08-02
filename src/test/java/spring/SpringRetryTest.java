package spring;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import spring.springRetry.RemoteService;

import javax.annotation.Resource;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/11/19
 */
public class SpringRetryTest extends BaseSpringTest {

    @Autowired
    private RemoteService remoteService;


    @Test
    public void test1() {
        remoteService.call();
    }

    @Test
    public void test2() throws Throwable {
        remoteService.call2();

    }
}
