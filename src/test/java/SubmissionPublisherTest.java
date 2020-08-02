import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author jujun chen
 * @date 2020/07/28
 */
public class SubmissionPublisherTest {

    @Test
    public void test1() {
        SubmissionPublisher<Integer> submissionPublisher = new SubmissionPublisher<>();
        submissionPublisher.consume(System.out::println);
        submissionPublisher.submit(1);
        submissionPublisher.submit(2);
        submissionPublisher.offer(3, (x, y) -> {
            System.out.println("xxx");
            return false;
        });
    }
}
