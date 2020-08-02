import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author jujun chen
 * @date 2020/07/18
 */
public class CompletableFutureTest {

   @SneakyThrows
   @Test
   public void test1() {
       CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> calc(50))
               .thenApply( i -> Integer.toString(i))
               .thenApply(str -> "\"" + str + "\"")
               .thenAccept(System.out::println);
       future.get();
   }

   @SneakyThrows
   public static Integer calc(Integer para) {
       TimeUnit.SECONDS.sleep(1);
       return para * para;
   }
}
