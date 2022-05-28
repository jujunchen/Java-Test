package spring;
import	java.beans.BeanInfo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import spring.transactionTest.PersonDatabaseTestsConfig;
import spring.transactionTest.TransactionTestService;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author: jujun chen
 * @Type 注解事务测试
 * @description:
 * @date: 2019/11/26
 */
public class TransactionTest extends BaseSpringTest{

    @Autowired
    private TransactionTestService testService;

    @Test
    public void test1() {
//        testService.method1();
        testService.method2();
    }

    @After
    public void verifyAfterRows() {
        assertEquals("验证数量失败",0, testService.selectRows());
    }

}
