package es;

import lombok.SneakyThrows;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
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
        //配置账号密码
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "Elastic123456"));

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
        //设置账号密码
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
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
        //设置request请求参数
        request.addParameter("pretty", "true");
//        request.setEntity(new NStringEntity());

        Response response = restClient.performRequest(request);
        System.out.println(response);
    }


    /**
     * 异步请求
     */
    @Test
    public void performRequestAsync() {
        Request request = new Request("GET", "/");
        restClient.performRequestAsync(request, new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

}
