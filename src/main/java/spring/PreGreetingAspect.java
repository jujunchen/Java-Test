package spring;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/07
 */
@Aspect
@Component
public class PreGreetingAspect {

    @Before("execution(* spring.NaiveWaiterService.greetTo(..)) && args(name)")
    public void beforeGreeting(String name) {
        System.out.println("execution...");
        System.out.println("name = " + name);
    }

    @Before("@annotation(w)")
    public void beforeGreeting2(WaiterTest w) {
        System.out.println("@annotation");
        System.out.println(w.getClass());
    }

    @Before("args(String)")
    public void argsTest() {
        //System.out.println("args");
    }

    @Before("@args(ClientName)")
    public void argsTest2() {
        System.out.println("@args");
    }

    @Before("target(Waiter)")
    public void targetTest() {
        System.out.println("target");
    }

    @Before("@target(org.springframework.stereotype.Service)")
    public void targetTest2() {
        //System.out.println("@target");
    }

    @Before("@within(WaiterService)")
    public void withinTest() {
        System.out.println("@within");
    }


    /**
     *  引介增强
     * @see SellerTest
     */
    @DeclareParents(value = "spring.NaiveWaiterService", defaultImpl = SmartSeller.class)
    public Seller seller;


}
