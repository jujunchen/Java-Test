package design.observer.flow;

/**
 * @author jujun chen
 * @date 2020/07/27
 */
public class FlowTest {

    public static void main(String[] args) {
        OneShotPublisher oneShotPublisher = new OneShotPublisher();
        oneShotPublisher.subscribe(new SampleSubscriber<>(Integer.MAX_VALUE,System.out::println));
    }
}
