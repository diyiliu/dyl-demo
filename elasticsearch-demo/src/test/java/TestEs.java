import com.alibaba.fastjson.JSON;
import com.diyiliu.es.model.Person;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    public void test1() throws Exception{
        String jsonObject = "{\"age\":10,\"dateOfBirth\":1471466076564,"
                +"\"fullName\":\"John Doe\"}";
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
    public void test2() throws Exception{
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
}
