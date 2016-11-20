package com.fengzii;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ElasticSearchHandler {
    private static Client client;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try {
            /* 创建客户端 */
            client = TransportClient.builder().build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

            //插入数据
            saveToEs();

            // Prepare and execute query
            SearchResponse resp = searchEs();

            SearchHits shs = resp.getHits();

            //update
            updateEs(shs);

            resp = searchEs();
            //delete
            deleteEs(shs);
            resp = searchEs();


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            client.close();
        }

    }

    /*
        删除传进来的SearchHits里所有的记录
     */
    private static void deleteEs(SearchHits shs) {
        for (int i = 0; i < shs.getTotalHits(); i++) {

            DeleteResponse response = client.prepareDelete("blog", "article1", shs.getAt(i).getId()).get();
            System.out.println(JSON.toJSONString(response));
        }
    }

    /*
    更新传进来的shs中的第一条记录，设置title为1git简介1111
     */
    private static void updateEs(SearchHits shs) throws IOException, InterruptedException, ExecutionException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(shs.getAt(0).getIndex());
        updateRequest.type(shs.getAt(0).getType());
        updateRequest.id(shs.getAt(0).getId());
        updateRequest.doc(jsonBuilder()
                .startObject()
                .field("title", "1git简介1111")
                .endObject());
        client.update(updateRequest).get();
    }

    /*
    查询title或content中带有git的记录
     */
    private static SearchResponse searchEs() {
        // Refresh index reader
        client.admin().indices().refresh(Requests.refreshRequest("_all")).actionGet();
        QueryBuilder queryBuilder = QueryBuilders.termQuery("title", "git");
        QueryBuilder queryBuilder1 = QueryBuilders.termQuery("content", "git");
        SearchResponse resp = client.prepareSearch("blog")
                .setTypes("article1")
                .setSearchType(SearchType.DEFAULT)
                .setQuery(queryBuilder).setQuery(queryBuilder1)
//                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();
        System.out.println(JSON.toJSONString(resp.getHits()));
        return resp;
    }

    /*
    存储数据进es
     */
    private static void saveToEs() {
        List<String> jsonData = DataFactory.getInitJsonData();

        for (int i = 0; i < jsonData.size(); i++) {
            IndexResponse response = client.prepareIndex("blog", "article1").setSource(jsonData.get(i)).get();
            if (response.isCreated()) {
                System.out.println("创建成功!23232");
            }
        }
    }
}