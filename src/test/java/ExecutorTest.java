import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/06/25
 */
public class ExecutorTest {

    @Test
    public void unconfigurableExecutorServiceTest() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 3, 0L,
                TimeUnit.SECONDS, new LinkedBlockingDeque<>());

        ExecutorService executorService = Executors.unconfigurableExecutorService(threadPoolExecutor);

        List<Callable<String>> list = new ArrayList<>();
        list.add(new TestCallable());
        List<Future<String>> futureList = executorService.invokeAll(list, 100, TimeUnit.MILLISECONDS);

        futureList.forEach(e -> {
            try {
                System.out.println(e.get());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

}

class TestCallable implements Callable {
    @Override
    public String call() throws Exception {
        return "111";
    }
}

class TestCallable2 implements Callable {
    @Override
    public String call() throws Exception {
        return "222";
    }
}
