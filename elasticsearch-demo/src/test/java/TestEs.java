import com.alibaba.fastjson.JSON;
import com.diyiliu.es.model.Person;
import com.google.common.collect.Maps;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.CollectionUtils;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Description: TestEs
 * Author: DIYILIU
 * Update: 2020-11-23 15:36
 */
public class TestEs {
    private List<Person> listOfPersons = new ArrayList();
    private RestHighLevelClient restHighLevelClient;

    private final static String HOST = "121.37.11.173";

    @Before
    public void setUp() throws IOException {
        Person person1 = new Person(10, "John Doe", new Date());
        Person person2 = new Person(25, "Janette Doe", new Date());
        listOfPersons.add(person1);
        listOfPersons.add(person2);

        restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost(HOST, 9200, "http")));
    }

    @Test
    public void test1() throws Exception {
        String jsonObject = "{\"age\":10,\"dateOfBirth\":1471466076564,"
                + "\"fullName\":\"John Doe\"}";
        IndexRequest request = new IndexRequest("people");
        request.source(jsonObject, XContentType.JSON);

        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        String index = response.getIndex();
        long version = response.getVersion();

        assertEquals(DocWriteResponse.Result.CREATED, response.getResult());
        assertEquals(1, version);
        assertEquals("people", index);
    }

    @Test
    public void test2() throws Exception {
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .field("fullName", "Test")
                .field("dateOfBirth", new Date().getTime())
                .field("age", "10")
                .endObject();

        IndexRequest request = new IndexRequest("people");
        request.source(builder);

        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        String index = response.getIndex();
        long version = response.getVersion();

        assertEquals(DocWriteResponse.Result.CREATED, response.getResult());
        assertEquals(1, version);
        assertEquals("people", index);
    }

    @Test
    public void test3() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        // 索引
        searchRequest.indices("people");

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHits = response.getHits().getHits();
        List<Person> results =
                Arrays.stream(searchHits)
                        .map(hit -> JSON.parseObject(hit.getSourceAsString(), Person.class))
                        .collect(Collectors.toList());

        results.forEach(res -> {
            System.out.println(JSON.toJSONString(res));
        });
    }


    @Test
    public void test4() {
        Map where = Maps.newHashMap();
        where.put("fullName", "test");

        List list = searchIndex("people", 0, 10, where, null, null, null, 500);
        System.out.println(JSON.toJSONString(list));
    }


    /**
     * @param index
     * @param from
     * @param size
     * @param where
     * @param sortFieldsToAsc
     * @param includeFields
     * @param excludeFields
     * @param timeOut
     * @return
     */
    public List<Map<String, Object>> searchIndex(String index, int from, int size, Map<String, Object> where,
                                                 Map<String, Boolean> sortFieldsToAsc, String[] includeFields, String[] excludeFields,
                                                 int timeOut) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            //条件
            if (where != null && !where.isEmpty()) {
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                where.forEach((k, v) -> {
                    if (v instanceof Map) {
                        //范围选择map  暂定时间
                        Map<String, Date> mapV = (Map<String, Date>) v;
                        if (mapV != null) {
                            boolQueryBuilder.must(
                                    QueryBuilders.rangeQuery(k).
                                            gte(format.format(mapV.get("start"))).
                                            lt(format.format(mapV.get("end"))));
                        }
                    } else {
                        //普通模糊匹配
                        boolQueryBuilder.must(QueryBuilders.wildcardQuery(k, v.toString()));
                    }
                });
                sourceBuilder.query(boolQueryBuilder);
            }

            //分页
            from = from <= -1 ? 0 : from;
            size = size >= 1000 ? 1000 : size;
            size = size <= 0 ? 15 : size;
            sourceBuilder.from(from);
            sourceBuilder.size(size);

            //超时
            sourceBuilder.timeout(new TimeValue(timeOut, TimeUnit.SECONDS));

            //排序
            if (sortFieldsToAsc != null && !sortFieldsToAsc.isEmpty()) {
                sortFieldsToAsc.forEach((k, v) -> {
                    sourceBuilder.sort(new FieldSortBuilder(k).order(v ? SortOrder.ASC : SortOrder.DESC));
                });
            } else {
                sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
            }

            //返回和排除列
            if (!CollectionUtils.isEmpty(includeFields) || !CollectionUtils.isEmpty(excludeFields)) {
                sourceBuilder.fetchSource(includeFields, excludeFields);
            }

            SearchRequest rq = new SearchRequest();
            //索引
            rq.indices(index);
            //各种组合条件
            rq.source(sourceBuilder);

            //请求
            System.out.println(rq.source().toString());
            SearchResponse rp = restHighLevelClient.search(rq, RequestOptions.DEFAULT);

            //解析返回
            if (rp.status() != RestStatus.OK || rp.getHits().getTotalHits().value <= 0) {
                return Collections.emptyList();
            }

            //获取source
            return Arrays.stream(rp.getHits().getHits()).map(b -> b.getSourceAsMap()).collect(Collectors.toList());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }
}
