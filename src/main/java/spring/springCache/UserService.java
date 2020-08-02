package spring.springCache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/10/22
 */
@Service
public class UserService {

    //@Cacheable(value = "userName",cacheManager = "simpleCacheManager")
    @Cacheable("userName")
    public String getUserNameById(long id) {
        System.out.println("查询数据库获取姓名");
        return "陈小侠";
    }

    @Cacheable("userAge")
    public int getUserAgeById(long id) {
        System.out.println("查询数据库获取年龄");
        return 18;
    }

    @CachePut("userName")
    public String  updateUserNameById(long id) {
        System.out.println("更新用户名");
        return "陈大侠";
    }

    @CacheEvict("userName")
    public void deleteUserNameById(long id) {
        System.out.println("删除用户名");
    }
}
