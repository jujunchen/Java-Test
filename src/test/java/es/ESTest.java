package es;

import lombok.SneakyThrows;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author jujun chen
 * @date 2020/08/26
 */
public class ESTest {

    private static final String HOST = "es-cn-6ja1st4nc00082bam.public.elasticsearch.aliyuncs.com";
    private static final Integer PORT = 9200;

    public static RestClient restClient = null;

    public ESTest() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(HOST, PORT, "http"));

        //设置请求头
        builder.setDefaultHeaders(new Header[] {new BasicHeader("header", "value")});
        //设置监听器，在每次节点失败时都能收到通知
        builder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node node) {
                super.onFailure(node);
            }
        });
        //配置节点选择器
        builder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);
        //设置超时时间
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                return requestConfigBuilder.setSocketTimeout(10000);
            }
        });
        restClient = builder.build();
    }

    @SneakyThrows
    @After
    public void after() {
        restClient.close();
    }


    /**
     * 同步请求
     */
    @SneakyThrows
    @Test
    public void performRequest() {
        Request request = new Request("GET", "/");

        Response response = restClient.performRequest(request);
        System.out.println(response);
    }
}
