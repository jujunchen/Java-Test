package spring;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import spring.springCache.UserService;

/**
 * @author: jujun chen
 * @Type
 * @description: spring cache 测试
 * @date: 2019/10/22
 */
public class SpringCacheTest extends BaseSpringTest {

    @Autowired
    UserService userService;

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



}
