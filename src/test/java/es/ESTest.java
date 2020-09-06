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
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
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
import org.elasticsearch.client.core.MultiTermVectorsRequest;
import org.elasticsearch.client.core.MultiTermVectorsResponse;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.sniff.SniffOnFailureListener;
import org.elasticsearch.client.sniff.Sniffer;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
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
    private static final String TWO_INDEX = "twoindex";

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
    @SneakyThrows
    @Test
    public void bulkIndex() {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest(INDEX).id("4").source("name", "小红", "sex", "女"));
        bulkRequest.add(new IndexRequest(INDEX).id("5").source("name", "小黄", "sex", "男"));
        bulkRequest.add(new IndexRequest(INDEX).id("8").source("name", "小黄8", "sex", "男", "age", 18));
        bulkRequest.add(new IndexRequest(INDEX).id("9").source("name", "小黄9", "sex", "男", "age", 19));

        //同步方式请求
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkResponse.status());

        //异步方式，通过ActionListener来监听

        //查询一下结果
        GetResponse getResponse = restHighLevelClient.get(new GetRequest(INDEX).id("5"), RequestOptions.DEFAULT);
        System.out.println(getResponse);
    }

    /**
     * bulkProcessor批处理操作
     */
    @SneakyThrows
    @Test
    public void bulkProcessor() {
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                //处理前
                System.out.println("Executing bulk " + executionId + " actions " + request.numberOfActions());
            }

            @SneakyThrows
            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                //处理后
                if (response.hasFailures()) {
                    System.out.println("Bulk " + executionId + "executed fail");
                } else {
                    System.out.println("Bulk " + executionId + "completed in " + response.getTook().getMillis() + " milliseconds");

                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                //批处理失败后
                failure.printStackTrace();
            }
        };

        BulkProcessor.Builder builder= BulkProcessor.builder((request, bulkListener)
                -> restHighLevelClient.bulkAsync(request, RequestOptions.DEFAULT, bulkListener), listener);

        builder.setBulkActions(500);
        builder.setFlushInterval(TimeValue.timeValueSeconds(10));

        BulkProcessor bulkProcessor = builder.build();

        bulkProcessor.add(new IndexRequest(INDEX).id("7").source("name", "小强"));
        bulkProcessor.add(new UpdateRequest(INDEX, "4").doc("name", "小黄"));

        bulkProcessor.flush();
        bulkProcessor.close();


       Thread.sleep(1000);

        GetResponse getResponse = restHighLevelClient.get(new GetRequest(INDEX, "7"), RequestOptions.DEFAULT);
        System.out.println(getResponse);
    }

    /**
     * multiGet 批处理请求
     */
    @SneakyThrows
    @Test
    public void multiGet() {
        MultiGetRequest multiGetRequest = new MultiGetRequest();
        multiGetRequest.add(new MultiGetRequest.Item(INDEX, "1"));
        multiGetRequest.add(new MultiGetRequest.Item(INDEX, "2"));
        multiGetRequest.add(new MultiGetRequest.Item(INDEX, "7"));


        MultiGetResponse multiResponse = restHighLevelClient.mget(multiGetRequest, RequestOptions.DEFAULT);
        for (MultiGetItemResponse response : multiResponse.getResponses()) {
            System.out.println(response.getResponse());
        }
    }

    /**
     * 文档重新索引
     */
    @SneakyThrows
    @Test
    public void reIndexRequest() {
        restHighLevelClient.indices().create(new org.elasticsearch.client.indices.CreateIndexRequest(TWO_INDEX), RequestOptions.DEFAULT);

        ReindexRequest reindexRequest = new ReindexRequest();
        reindexRequest.setSourceIndices(INDEX);
        //towIndex 文档索引必须先存在
        reindexRequest.setDestIndex(TWO_INDEX);

        BulkByScrollResponse bulkByScrollResponse = restHighLevelClient.reindex(reindexRequest, RequestOptions.DEFAULT);
        System.out.println(bulkByScrollResponse.getSearchFailures());
    }

    /**
     * 检测是否已经将数据复制到了twoindex索引中
     */
    @SneakyThrows
    @Test
    public void getResponseById6() {
        GetResponse getResponse = restHighLevelClient.get(new GetRequest(TWO_INDEX).id("7"), RequestOptions.DEFAULT);
        System.out.println(getResponse);
    }

    /**
     * 查询时更新，不知道这个接口有什么用？
     */
    @SneakyThrows
    @Test
    public void updateByQueryRequest() {
        UpdateByQueryRequest updateByQueryRequest = new UpdateByQueryRequest(INDEX);

        BulkByScrollResponse bulkByScrollResponse = restHighLevelClient.updateByQuery(updateByQueryRequest, RequestOptions.DEFAULT);
        System.out.println(bulkByScrollResponse);
    }

    /**
     * 文档查询时删除,为什么没有效果？？
     */
    @SneakyThrows
    @Test
    public void deleteByQueryRequest() {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(INDEX);

        //id=4
        deleteByQueryRequest.setQuery(new TermQueryBuilder("id", "4"));
        deleteByQueryRequest.setRefresh(true);

        BulkByScrollResponse bulkByScrollResponse = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        System.out.println(bulkByScrollResponse);

        //查询一下是否删除了
        GetResponse getResponse = restHighLevelClient.get(new GetRequest(INDEX).id("4"), RequestOptions.DEFAULT);
        System.out.println("id = 4: " + getResponse);
    }

    /**
     * 批量获取词向量
     */
    @SneakyThrows
    @Test
    public void multiTermRequest() {
        //1
        MultiTermVectorsRequest termVectorsRequest = new MultiTermVectorsRequest();
        String[] ids = new String[]{"7", "4"};
        for (String id : ids) {
            TermVectorsRequest request = new TermVectorsRequest(INDEX, id);
            request.setFields("name");
            termVectorsRequest.add(request);
        }
        MultiTermVectorsResponse response = restHighLevelClient.mtermvectors(termVectorsRequest, RequestOptions.DEFAULT);
        for (TermVectorsResponse termVectorsRespons : response.getTermVectorsResponses()) {
            System.out.println(termVectorsRespons.getIndex());
        }

        //方法2
        TermVectorsRequest termVectorsRequest2 = new TermVectorsRequest(INDEX, "1");
        termVectorsRequest2.setFields("name");
        termVectorsRequest = new MultiTermVectorsRequest(ids, termVectorsRequest2);
        response = restHighLevelClient.mtermvectors(termVectorsRequest, RequestOptions.DEFAULT);
        for (TermVectorsResponse termVectorsRespons : response.getTermVectorsResponses()) {
            System.out.println(termVectorsRespons.getId());
        }
    }

    /**
     * 搜索请求
     */
    @SneakyThrows
    @Test
    public void searchRequest() {
        SearchRequest searchRequest = new SearchRequest(INDEX);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        默认，按分数排序
//        searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        //按ID排序
        searchSourceBuilder.sort(new FieldSortBuilder("_id").order(SortOrder.ASC));
        //关闭源筛选
//        searchSourceBuilder.fetchSource(false);

        searchSourceBuilder.query(QueryBuilders.termQuery("sex", "男"));
        //设置高亮显示

        //请求聚合
//        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.avg("avg_age").field("age");
        AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("avg_age").field("age");
//        termsAggregationBuilder.subAggregation();
        searchSourceBuilder.aggregation(avgAggregationBuilder);

        //建议请求


        searchRequest.source(searchSourceBuilder);

        //从0开始搜索
        searchSourceBuilder.from(0);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


        SearchHits hits = searchResponse.getHits();
        //搜索结果总数量
        System.out.println(hits.getTotalHits().value);

        for (SearchHit hit : hits.getHits()) {
            //json形式返回结果
            System.out.println(hit.getSourceAsString());
            //返回键值对的形式
            Map<String, Object> stringObjectMap = hit.getSourceAsMap();
        }

        //搜索结果聚合
        Aggregations aggregations = searchResponse.getAggregations();
        Map<String, Aggregation> byAgeAvg = aggregations.getAsMap();
        System.out.println("avg_age = " + ((ParsedAvg)byAgeAvg.get("avg_age")).getValue());
    }

    /**
     * 滚动搜索
     */
    @SneakyThrows
    @Test
    public void scrollSearchRequest() {
        SearchRequest searchRequest = new SearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchQuery("sex", "男"));
        searchSourceBuilder.size(3);

        searchRequest.source(searchSourceBuilder);

        //设置滚动间隔
        searchRequest.scroll(TimeValue.timeValueSeconds(1));

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        //返回滚动的ID
        String scrollId = searchResponse.getScrollId();

        //检索第一批结果
        SearchHits hits = searchResponse.getHits();

        for (SearchHit hit : hits.getHits()) {
            System.out.println(hit.getSourceAsString());
        }

        for (;hits != null && hits.getHits().length != 0;) {
            //设置滚动标识
            SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
            //设置搜索滚动的过期时间
//            scrollRequest.scroll(TimeValue.timeValueSeconds(1));
            //模拟滚动Id超时
//            Thread.sleep(3000);
            SearchResponse searchResponse1 = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);

            hits = searchResponse1.getHits();

            for (SearchHit hit : hits.getHits()) {
                System.out.println(hit.getSourceAsString());
            }
        }
    }

    /**
     * 批量搜索
     */
    @SneakyThrows
    @Test
    public void multiSearchRequest() {
        MultiSearchRequest multiSearchRequest = new MultiSearchRequest();

        SearchRequest searchRequest1 = new SearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("name", "小红")));
        searchSourceBuilder.query(QueryBuilders.matchQuery("name", "小红"));
        searchRequest1.source(searchSourceBuilder);
        multiSearchRequest.add(searchRequest1);

        SearchRequest searchRequest2 = new SearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder2 = new SearchSourceBuilder();
        searchSourceBuilder2.query(QueryBuilders.termQuery("name", "小陈"));
        searchRequest2.source(searchSourceBuilder2);
        multiSearchRequest.add(searchRequest2);

        MultiSearchResponse multiSearchResponse = restHighLevelClient.msearch(multiSearchRequest, RequestOptions.DEFAULT);

        for (MultiSearchResponse.Item item : multiSearchResponse.getResponses()) {
            SearchResponse searchResponse = item.getResponse();
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits.getHits()) {
                System.out.println(hit.getSourceAsString());
            }
        }
    }

}
