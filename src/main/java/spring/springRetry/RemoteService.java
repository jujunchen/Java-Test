package spring.springRetry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: jujun chen
 * @Type
 * @description:
 * @date: 2019/11/19
 */
@Service
//@Slf4j
public class RemoteService {


    /**
     * 模拟方法调用
     */
    @Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 5000L, multiplier = 2))
    public void call() {
//        log.info("Call something...");
        throw new RuntimeException("RPC调用异常");
    }

    public void call2() throws Throwable {
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(3);

        retryTemplate.setRetryPolicy(simpleRetryPolicy);

       retryTemplate.execute(new RetryCallback<Object, Throwable>() {
            @Override
            public Object doWithRetry(RetryContext context) throws Throwable {
//                log.info("Call something");
                throw new RuntimeException("RPC调用异常");
            }
        }, new RecoveryCallback<Object>() {
            @Override
            public Object recover(RetryContext context) throws Exception {
//                log.info("Start do recover things...");
                return null;
            }
        });
    }

    /**
     * 恢复处理
     * @param e
     */
    @Recover
    public void recover(RuntimeException e) {
//        log.info("Start do recover things...");
//        log.warn("We meet ex", e);
    }


    /**
     * 没有重试
     * @param args
     */
    public static void main(String[] args) {
        RemoteService remoteService = new RemoteService();
        remoteService.call();
    }

}
