import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: jujun chen
 * @Type
 * @description: guava local cache
 * @date: 2019/07/24
 */
public class GuavaLocalCacheTest {

    private static final Cache<String,String> cache = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.SECONDS).build();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        cache.put("a" , "a");
        cache.put("b" , "b");
        Thread.sleep(5000);
        System.out.println(cache.getIfPresent("a"));
    }
}
