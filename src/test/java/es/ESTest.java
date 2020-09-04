package es;

import lombok.SneakyThrows;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.sniff.SniffOnFailureListener;
import org.elasticsearch.client.sniff.Sniffer;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jujun chen
 * @date 2020/08/26
 */
public class ESTest {

    private static final String HOST = "es-cn-6ja1st4nc00082bam.public.elasticsearch.aliyuncs.com";
    private static final Integer PORT = 9200;
    public static final String PROTOCOL = "http";

    //初级客户端
    private RestClient restClient;
    //高级客户端
    private RestHighLevelClient restHighLevelClient;

    public ESTest() {
        //初级客户端初始化
        primaryClientInit();
        //高级客户端初始化
        highLevelClientInit();
    }

    private void highLevelClientInit() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(HOST, PORT, PROTOCOL));
        //配置账号密码
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "Elastic123456"));
        //设置账号密码
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                        //修改线程数量
                        .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(5).build());
            }
        });
        //可以通过低级客户端构建高级客户端
        restHighLevelClient = new RestHighLevelClient(builder);
    }

    public void primaryClientInit() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(HOST, PORT, PROTOCOL));
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
                return requestConfigBuilder.setSocketTimeout(10000).setConnectTimeout(5000);
            }
        });
        //设置账号密码
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                        //修改线程数量
                        .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(5).build());
            }
        });
        //手动配置节点选择器
        /*builder.setNodeSelector(new NodeSelector() {
            @Override
            public void select(Iterable<Node> iterable) {

            }
        });*/
        //配置失败嗅探器，先配置监听器
        SniffOnFailureListener sniffOnFailureListener = new SniffOnFailureListener();
        builder.setFailureListener(sniffOnFailureListener);

        restClient = builder.build();

        //配置嗅探器，能自动发现集群中运行的节点，配置为现有RestClient的实例
        Sniffer sniffer = Sniffer.builder(restClient)
                //配置5分钟更新一次 节点
                .setSniffIntervalMillis(5 * 60 * 1000)
                //失败后延迟1分钟
                .setSniffAfterFailureDelayMillis(60000).build();
        //配置失败时启用嗅探器，更新节点列表
        sniffOnFailureListener.setSniffer(sniffer);

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
        //设置HttpEntity
        request.setEntity(new NStringEntity("{\"json\":\"text\"}", ContentType.APPLICATION_JSON));
        //设置json字符串
        request.setJsonEntity("{\"json\":\"text\"}");

        //这种方式不能正确验证
        /*RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addHeader("Authorization", "Basic ZWxhc3RpYyUzQUVsYXN0aWMxMjM0NTY=");
        builder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024)
        );
        RequestOptions requestOptions = builder.build();
        request.setOptions(requestOptions);*/

        Response response = restClient.performRequest(request);
        System.out.println(response);
        //输出返回结果
        System.out.println(EntityUtils.toString(response.getEntity()));
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

    private static final String INDEX = "firstindex";

    /**
     * 使用高级客户端创建索引
     */
    @SneakyThrows
    @Test
    public void createIndex() {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(INDEX);
        boolean indexIsExist = restHighLevelClient.indices().exists(new GetIndexRequest(INDEX), RequestOptions.DEFAULT);
        if (!indexIsExist) {
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            System.out.println(createIndexResponse.index());
        }
    }

    /**
     * 添加数据
     */
    @SneakyThrows
    @Test
    public void addData() {
        IndexRequest request = new IndexRequest(INDEX);
        request.id("1");
        //String创建
        String jsonStr = "{\"name\":\"小红\",\"sex\":\"女\"}";
        request.source(jsonStr, XContentType.JSON);

        IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse);

        //Map创建
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("name", "小明");
        jsonMap.put("sex", "男");
        request.id("2").source(jsonMap);
        indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse);

        //基于XContentBuilder创建
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        builder.field("name", "小刘");
        builder.field("sex", "男");
        builder.endObject();
        request.id("3").source(builder);
        indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse);

        //使用键值对的形式,必须是偶数个数
        request.id("4").source("name", "小强", "sex","男");
        indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse);
        //异步
        ActionListener actionListener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                //执行成功
                System.out.println("异步执行成功：" + indexResponse);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        };
        restHighLevelClient.indexAsync(request, RequestOptions.DEFAULT, actionListener);
        Thread.sleep(1000);
     }

    /**
     * 获取数据
     */
    @SneakyThrows
    @Test
    public void getData() {
        GetRequest getRequest = new GetRequest(INDEX);
        getRequest.id("1");
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse);

        String[] includes = Strings.EMPTY_ARRAY;
        String[] excludes = new String[] {"name"};
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        getRequest.fetchSourceContext(fetchSourceContext);
        getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse);
    }

    /**
     * 校验某个文档是否存在于索引中
     */
    @SneakyThrows
    @Test
    public void checkExistIndex() {
        GetRequest getRequest = new GetRequest(INDEX, "1");
        //同步方式
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        //异步方式
//        restHighLevelClient.existsAsync();
        assert exists;

        /**
         * 删除以后再判断是否存在
         */
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, "1");
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse);
        exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        assert !exists;
    }

    /**
     * 更新文档数据
     */
    @SneakyThrows
    @Test
    public void updateIndexDocuments() {
        UpdateRequest updateRequest = new UpdateRequest(INDEX, "2");
        //同样提供几种数据格式来更新数据
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "小陈1");
        jsonMap.put("sex", "男");
        updateRequest.doc(jsonMap);
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse);
        GetResponse getResponse = restHighLevelClient.get(new GetRequest(INDEX, "2"), RequestOptions.DEFAULT);
        System.out.println(getResponse);

        //如果更新文档不存在，则插入
        updateRequest = new UpdateRequest(INDEX, "1");
        IndexRequest indexRequest = new IndexRequest(INDEX);
        indexRequest.source(jsonMap);
        //不存在就执行indexRequest
        updateRequest.doc(jsonMap).upsert(indexRequest);
        restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        getResponse = restHighLevelClient.get(new GetRequest(INDEX, "1"), RequestOptions.DEFAULT);
        System.out.println(getResponse);
    }

    /**
     * 获取文档词向量,词的统计信息
     */
    @SneakyThrows
    @Test
    public void getTermVectors() {
        TermVectorsRequest request = new TermVectorsRequest(INDEX, "1");
        request.setFields("name");

        TermVectorsResponse response = restHighLevelClient.termvectors(request, RequestOptions.DEFAULT);
        System.out.println(response.getIndex());
        System.out.println(response.getDocVersion());
    }

    /**
     * 批量添加数组
     */
    @Test
    public void bulkIndex() {

    }

}
