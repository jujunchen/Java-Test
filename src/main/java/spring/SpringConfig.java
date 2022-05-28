package spring;
import	java.text.SimpleDateFormat;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/07
 */
@Configuration
@ComponentScan("spring")
@EnableAspectJAutoProxy(proxyTargetClass = true)
//@EnableCaching
//@EnableRetry
@EnableTransactionManagement
public class SpringConfig {

    
    /*@Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        return cacheManager;
    }*/

    /*@Bean("simpleCacheManager")
    public SimpleCacheManager simpleCacheName() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("userName")));
        return simpleCacheManager;
    }*/

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .addScript("classpath:schema.sql")
                .build();
    }

    @Bean
    public CacheManager compositeCacheManager() {
        CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();

        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("userName")));

        compositeCacheManager.setCacheManagers(Arrays.asList(cacheManager, simpleCacheManager));

        return compositeCacheManager;
    }


}
