package spring;

import cn.hutool.core.thread.NamedThreadFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import spring.springCache.UserService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: jujun chen
 * @Type
 * @description: spring cache 测试
 * @date: 2019/10/22
 */
public class SpringCacheTest extends BaseSpringTest {

    @Autowired
    UserService userService;

    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(5, 10, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Test
    public void getUserNameTest() {
        String userName = userService.getUserNameById(1);
        System.out.println(userName);
        String cacheUserName = userService.getUserNameById(1);
        System.out.println(cacheUserName);

        int age = userService.getUserAgeById(1);
        System.out.println(age);
        int cacheAge = userService.getUserAgeById(1);
        System.out.println(cacheAge);

        userService.updateUserNameById(1);
        String newCacheName = userService.getUserNameById(1);
        System.out.println(newCacheName);

        userService.deleteUserNameById(1);
        String newCacheName2 = userService.getUserNameById(1);
        System.out.println(newCacheName2);


    }


    @Test
    public void getTest() throws InterruptedException {
        threadPoolExecutor.submit(new Thread1());
        TimeUnit.SECONDS.sleep(1);
    }

    class Thread1 implements Runnable {


        @Override
        public void run() {
            String userName = userService.getUserNameById(1);
            System.out.println(userName);
        }
    }




}
