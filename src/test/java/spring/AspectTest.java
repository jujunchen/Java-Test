package spring;

import cn.hutool.core.thread.ThreadUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/07
 */

public class AspectTest extends BaseSpringTest {



    @Autowired
    Waiter naiveWaiter;

    @Test
    public void aspectTest() {
        naiveWaiter.greetTo("陈大侠");
        System.out.println("---------");

        naiveWaiter.serveTo("陈大侠");
        System.out.println("---------");

        Person person = new Person();
        person.name = "陈大侠";
        //naiveWaiter.serverTo2(person);
        System.out.println("---------");

        //naiveWaiter.haisay();
        System.out.println("---------");

        Seller seller = (Seller) naiveWaiter;
        seller.sell("苹果");
    }


    @Test
    public void test2() {
        Executor executor = Executors.newFixedThreadPool(200);
        int i = 0;
        for (i = 0; i < 200; i++) {
            Blue blue = new Blue(i);
            executor.execute(() -> naiveWaiter.greetTo("陈大侠" + blue.getI()));
        }

        ThreadUtil.sleep(1, TimeUnit.HOURS);
    }

    @Data
    @AllArgsConstructor
    class Blue {
        private int i;

    }

}
