package spring;


import org.springframework.stereotype.Service;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/09/07
 */
@Service
@WaiterService
public class NaiveWaiterService implements Waiter {
    @Override
    public void greetTo(String clientName) {
//        System.out.println("NaiveWaiter:greet to " + clientName + "...");
    }

    @Override
    @WaiterTest
    public void serveTo(String clientName) {
        System.out.println("NaiveWaiter:serving " + clientName + "...");
    }

    public void serverTo2(Person person) {
        System.out.println("NaiveWaiter:serving " + person.name + "...");
    }


}
