package com.redgo.dao;

import com.redgo.entity.FileWord;
import com.redgo.entity.Event;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FileDAOImpl implements FileDAO{

    @Autowired
    private SolrClient solrClient;

    @Override
    public HashMap<String, Object> findAll(String queryString, String page, String powerStation, String unit, String docType){

        HashMap<String, Object> response = null;
        try {
            response = new HashMap<>();

            Integer pageNumb = Integer.parseInt(page);

            SolrQuery params = new SolrQuery();

            if(docType.equals("JIT")){
                params.setQuery(queryString)
                        .setFilterQueries("JIT_text:" + powerStation,"JIT_text:" + unit)
                        .setSort("score", SolrQuery.ORDER.desc)
                        .setHighlight(true)//开启高亮
                        .setHighlightSimplePre("<span style='color:red;'>")//高亮的开始标签
                        .setHighlightSimplePost("</span>")//高亮的结束标签
                        .addHighlightField("id,JIT_text")//高亮字段
                        .set("df","JIT_keyWord")
                        .set("start", pageNumb * 14)//开始条数
                        .set("rows", 14)
                        .set("fl", "*,score");//指定查询结果中只存在哪些域;//每页显示条数
    //                    .set("defType","edismax")
    //                    .set("mm","100%");
            }else{
                params.setQuery(queryString)
                        .setFilterQueries("event_nuclearPlant:" + powerStation,"event_unitName:" + unit)
                        .setSort("score", SolrQuery.ORDER.desc)
                        .setHighlight(true)//开启高亮
                        .setHighlightSimplePre("<span style='color:red;'>")//高亮的开始标签
                        .setHighlightSimplePost("</span>")//高亮的结束标签
                        .addHighlightField("event_Name,event_overView")//高亮字段
                        .set("df","event_keyWord")
                        .set("start", pageNumb * 14)//开始条数
                        .set("rows", 14)
                        .set("fl", "*,score");//指定查询结果中只存在哪些域;//每页显示条数
    //                    .set("defType","edismax")
    //                    .set("mm","100%");
            }


            QueryResponse queryResponse = solrClient.query(params);
            SolrDocumentList results = queryResponse.getResults();
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();//获取高亮的结果
            System.out.println("总条数：" + results.getNumFound());


            if(docType.equals("JIT")){
                List<FileWord> fileWords = new ArrayList<FileWord>();

                for (SolrDocument result : results) {
                    FileWord fileWord = new FileWord();

                    fileWord.setAuthor(result.get("JIT_author").toString());
                    Map<String, List<String>> listMap = highlighting.get(result.get("id"));

                    //处理id高亮
                    if(listMap.containsKey("id")){
                        fileWord.setFile(listMap.get("id").get(0).toString());
                    }else{
                        fileWord.setFile(result.get("id").toString());
                    }
                    //处理file_text高亮
                    if(listMap.containsKey("JIT_text")){
                        fileWord.setText(listMap.get("JIT_text").get(0).toString());
                    }else{
                        fileWord.setText(result.get("JIT_text").toString());
                    }

                    fileWords.add(fileWord);
                }
                response.put("fileWords", fileWords);

                List<Event> events = new ArrayList<Event>();
                response.put("events", events);

            }else{
                List<Event> events = new ArrayList<Event>();

                for (SolrDocument result : results) {
                    Event event = new Event();

                    Map<String, List<String>> listMap = highlighting.get(result.get("id"));

                    //处理id高亮
                    if (listMap.containsKey("event_Name")) {
                        event.setEventName(listMap.get("event_Name").get(0).toString());
                    } else {
                        event.setEventName(result.get("event_Name").toString());
                    }
                    //处理file_text高亮
                    if (listMap.containsKey("event_overView")) {
                        event.setOverView(listMap.get("event_overView").get(0).toString());
                    } else {
                        event.setOverView(result.get("event_overView").toString());
                    }

                    events.add(event);
                }
                response.put("events", events);

                List<FileWord> fileWords = new ArrayList<FileWord>();
                response.put("fileWords", fileWords);
            }
            response.put("sumNumb", results.getNumFound());
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
