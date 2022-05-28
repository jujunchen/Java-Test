package spring.transactionTest;

import lombok.extern.slf4j.Slf4j;
import org.drools.core.command.assertion.AssertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/11/26
 */
@Service
@Slf4j
public class TransactionTestService {

    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Spring 事务注解
     */
    @Transactional(rollbackFor = Exception.class)
    public void method1() {
        log.info("Spring 事务注解");
        Assert.isTrue(TransactionSynchronizationManager.isActualTransactionActive(), "不在事务中");
        addPerson(jdbcTemplate, "ali");
        addPerson(jdbcTemplate, "jujunchen");
        throw new RuntimeException("method1发生运行异常");
    }

    /**
     * JavaEE 事务注解
     */
    @javax.transaction.Transactional
    public void method2() {
        //log.info("Spring 事务注解");
        addPerson(jdbcTemplate, "jujunchen");
        throw new RuntimeException("method2发生运行异常");
    }

    /**
     * 查询总数
     * @return
     */
    public int selectRows() {
        return countRowsInPersonTable(jdbcTemplate);
    }


    protected static int clearPersonTable(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.update("DELETE FROM person");
    }

    protected static int countRowsInPersonTable(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForObject("SELECT COUNT(0) FROM person", Integer.class);
    }

    protected static int addPerson(JdbcTemplate jdbcTemplate, String name) {
        return jdbcTemplate.update("INSERT INTO person VALUES(?)", name);
    }

    protected static int deletePerson(JdbcTemplate jdbcTemplate, String name) {
        return jdbcTemplate.update("DELETE FROM person WHERE name=?", name);
    }

}
