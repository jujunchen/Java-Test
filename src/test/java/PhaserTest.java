import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * @author jujun chen
 * @date 2020/06/30
 */
public class PhaserTest {

    Phaser phaser = new Phaser(2);

    ExecutorService executor = Executors.newFixedThreadPool(2);

   @Test
   public void test1() {
       executor.submit(() -> {
           System.out.println(Thread.currentThread().getName() + " step1");
           //等同 countDown()
           phaser.arrive();
       });

       executor.submit(() -> {
           System.out.println(Thread.currentThread().getName() + " step2");
           phaser.arrive();
       });

       //等同await()
       phaser.awaitAdvance(phaser.getPhase());
       System.out.println("thread end");
   }
}
