import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: jujun chen
 * @description:
 * @date: 2019/4/10
 */
public class ThreadExecutorTest {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor =  new ThreadPoolExecutor(0,5,30L, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());

        /*executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "======1=======");
            }
        });*/


        executor.execute(new Run1());

        //executor.shutdownNow();

        executor.execute(new Run1());

        TimeUnit.SECONDS.sleep(30);

    }

}

class Run1 implements Runnable{

    @Override
    public void run() {
        int count = 0;
        while (true){
            count++;

            System.out.println(Thread.currentThread().getName() + "======2======="+count);

            if (count == 10){
               /* try{
                    System.out.println(1/0);
                }catch (Exception ex){
                    ex.printStackTrace();
                }*/
                //System.out.println(1/0);
            }

            if (count == 20){
                System.out.println(count);
                break;
            }

            /*try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
}
