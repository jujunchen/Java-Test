package design.observer.flow;

import java.util.concurrent.Flow;
import java.util.function.Consumer;

/**
 * 消费者
 * @author jujun chen
 * @date 2020/07/27
 */
class SampleSubscriber<T> implements Flow.Subscriber<T> {
    final Consumer<? super T> consumer;
    Flow.Subscription subscription;
    final long bufferSize;
    long count;

    SampleSubscriber(long bufferSize, Consumer<? super T> consumer) {
        this.bufferSize = bufferSize;
        this.consumer = consumer;
    }


    /**
     * Publisher在被指定一个新的Subscriber时调用此方法。
     * 一般来说你需要在subscriber内部保存这个subscription实例，因为后面会需要通过她向publisher
     * 发送信号来完成：请求更多数据，或者取消订阅。
     */
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        long initialRequestSize = bufferSize;
        count = bufferSize - bufferSize / 2; // re-request when half consumed
        (this.subscription = subscription).request(initialRequestSize);
    }

    /**
     * 每当新的数据产生，这个方法会被调用
     * @param item
     */
    @Override
    public void onNext(T item) {
        if (--count <= 0)
            subscription.request(count = bufferSize - bufferSize / 2);
        consumer.accept(item);
    }

    /**
     * 当publisher出现异常时会调用subscriber的这个方法
     * @param ex
     */
    @Override
    public void onError(Throwable ex) { ex.printStackTrace(); }

    /**
     * 当publisher数据推送完毕时会调用此方法，于是整个订阅过程结束。
     */
    @Override
    public void onComplete() {
        System.out.println("has finished!");
    }
}
