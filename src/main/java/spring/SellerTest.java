package spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/07
 */
public class SellerTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);

        //必须要强转成接口，否则无法增强
        Waiter waiter = (Waiter) ctx.getBean("naiveWaiterService");
        waiter.greetTo("陈大侠");

        Seller seller = (Seller) waiter;
        seller.sell("苹果");
    }
}
