package spring;

import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import spring.springCache.UserService;

import java.util.concurrent.ArrayBlockingQueue;
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
    private UserService userService;

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

    private  static ThreadPoolExecutor threadPoolExecutor ;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(3,
                10, 2,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @SneakyThrows
    @Test
    public void threadTest() {
        threadPoolExecutor.submit(new Test123());
        TimeUnit.MINUTES.sleep(1);
    }

    class Test123 implements Runnable {
        @Override
        public void run() {
            System.out.println(SpringCacheTest.this.userService.getUserNameById(1));
        }
    }

}
