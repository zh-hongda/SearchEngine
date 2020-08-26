package testsolr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class TestSolr {

    //增
    @Test
    public void testAddDocument() throws IOException, SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer("http://localhost:8989/solr/collection1");
        SolrInputDocument doc = new SolrInputDocument();

        doc.addField("id","21");
        doc.addField("file_text","test");

        solrServer.add(doc);
        solrServer.commit();
    }

    //删
    @Test
    public void testDeleteDocument() throws IOException, SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer("http://localhost:8989/solr/collection1");

        solrServer.deleteById("21");
        solrServer.commit();
    }

    //基本查询方法
    @Test
    public void testQueryBasic() throws SolrServerException, IOException {
        HttpSolrServer solrServer = new HttpSolrServer("http://localhost:8989/solr/collection1");

        //创建查询对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        solrQuery.set("q","*:*");

        QueryResponse queryResponse = solrServer.query(solrQuery);

        //获取响应结果
        SolrDocumentList results = queryResponse.getResults();
        System.out.println("总记录数："+results.getNumFound());
        for(SolrDocument result : results){
            System.out.println(result.get("id"));
            System.out.println(result.get("file_author"));
            System.out.println("==================================");
        }
    }

    //指定默认查询域
    @Test
    public void testDefaultQuery() throws SolrServerException, IOException {
        HttpSolrServer solrServer = new HttpSolrServer("http://localhost:8989/solr/collection1");

        //创建查询对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        //solrQuery.set("q","*:*");

        //指定默认域查询
        solrQuery.set("df", "id");
        solrQuery.setQuery("21");

        QueryResponse queryResponse = solrServer.query(solrQuery);

        //获取响应结果
        SolrDocumentList results = queryResponse.getResults();
        System.out.println("总记录数："+results.getNumFound());
        for(SolrDocument result : results){
            System.out.println(result.get("id"));
            System.out.println(result.get("file_author"));
            System.out.println("==================================");
        }
    }

    //分页查询
    @Test
    public void testQueryPage() throws SolrServerException, IOException {
        HttpSolrServer solrServer = new HttpSolrServer("http://localhost:8989/solr/collection1");

        //创建查询对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        //solrQuery.set("q","*:*");

        //指定默认域查询
        solrQuery.set("df", "file_text");
        solrQuery.setQuery("经验");

        //分页
        int pageNow = 1;
        int pageSize = 8;
        solrQuery.set("start", (pageNow-1)*pageSize);//指定起始条数
        solrQuery.set("rows", pageSize);//指定每页显示的条数
        //指定查询结果中只存在哪些域
        solrQuery.set("fl", "id");

        QueryResponse queryResponse = solrServer.query(solrQuery);

        //获取响应结果
        SolrDocumentList results = queryResponse.getResults();
        System.out.println("总记录数：" + results.getNumFound());
        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("file_author"));
            System.out.println("==================================");
        }
    }

    //指定查询过滤条件
    @Test
    public void testQueryFilter() throws SolrServerException, IOException {
        HttpSolrServer solrServer = new HttpSolrServer("http://localhost:8989/solr/collection1");

        //创建查询对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        //solrQuery.set("q","*:*");

        //指定默认域查询
        solrQuery.set("df", "file_text");
        solrQuery.setQuery("经验");

        //分页
        int pageNow = 1;
        int pageSize = 8;
        solrQuery.set("start", (pageNow-1)*pageSize);//指定起始条数
        solrQuery.set("rows", pageSize);//指定每页显示的条数

        solrQuery.set("fl", "*");//指定查询结果中只存在哪些域

        //如下两个过滤条件是“或”关系，即，满足一条件就可以输出。
//        solrQuery.set("fq", "file_author:liuli");//指定查询时的过滤条件
//        solrQuery.set("fq", "price:[* to 10]liuli");//指定查询时的过滤条件为区间
        //如下两个过滤条件是“与”关系，即，需要同时满足才能输出
        solrQuery.setFilterQueries("file_author:liuli", "id:21");

        QueryResponse queryResponse = solrServer.query(solrQuery);

        //获取响应结果
        SolrDocumentList results = queryResponse.getResults();
        System.out.println("总记录数：" + results.getNumFound());
        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("file_author"));
            System.out.println("==================================");
        }
    }

    //按照指定域排序
    @Test
    public void testQuerySort() throws SolrServerException, IOException {
        HttpSolrServer solrServer = new HttpSolrServer("http://localhost:8989/solr/collection1");

        //创建查询对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        //solrQuery.set("q","*:*");

        //指定默认域查询
        solrQuery.set("df", "file_text");
        solrQuery.setQuery("经验");

        //分页
        int pageNow = 1;
        int pageSize = 8;
        solrQuery.set("start", (pageNow-1)*pageSize);//指定起始条数
        solrQuery.set("rows", pageSize);//指定每页显示的条数

        solrQuery.set("fl", "*,score");//指定查询结果中只存在哪些域

        solrQuery.setSort("score", SolrQuery.ORDER.asc);//按照指定域升序


        QueryResponse queryResponse = solrServer.query(solrQuery);

        //获取响应结果
        SolrDocumentList results = queryResponse.getResults();
        System.out.println("总记录数：" + results.getNumFound());
        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("file_author"));
            System.out.println(result.get("score"));
            System.out.println("==================================");
        }
    }

    //查询结果高亮
    @Test
    public void testQueryHighlight() throws SolrServerException, IOException {
        HttpSolrServer solrServer = new HttpSolrServer("http://localhost:8989/solr/collection1");

        //创建查询对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件
        //solrQuery.set("q","*:*");

        //指定默认域查询
        solrQuery.set("df", "file_text");
        solrQuery.setQuery("经验");

        //分页
        int pageNow = 1;
        int pageSize = 8;
        solrQuery.set("start", (pageNow-1)*pageSize);//指定起始条数
        solrQuery.set("rows", pageSize);//指定每页显示的条数

        solrQuery.set("fl", "*,score");//指定查询结果中只存在哪些域

        solrQuery.setSort("score", SolrQuery.ORDER.asc);//按照指定域升序

        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre("<span style='color:red;'>");
        solrQuery.setHighlightSimplePost("</span>");
        solrQuery.addHighlightField("file_text");

        QueryResponse queryResponse = solrServer.query(solrQuery);

        //获取响应结果
        SolrDocumentList results = queryResponse.getResults();
        //获取高亮的结果
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

        System.out.println("总记录数：" + results.getNumFound());
        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("file_author"));
            System.out.println(result.get("score"));
            System.out.println("============高亮之后的结果===============");
            Map<String, List<String>> listMap = highlighting.get(result.get("id"));
            if(listMap.containsKey("file_text")){
                System.out.println(listMap.get("file_text").get(0));
            }
        }
    }

    //综合查询
    @Test
    public void testQueryALL() throws SolrServerException, IOException {
        HttpSolrServer solrServer = new HttpSolrServer("http://localhost:8989/solr/collection1");
        SolrQuery solrQuery = new SolrQuery();//创建查询对象

        //在“file_text”中查询包含“经验”的项，同时要求“file_author”为“liuli”,查询结果高亮，并按照“score”升序排列。
        solrQuery.setQuery("经验")
                .setFilterQueries("file_author:liuli")//过滤条件
                .setSort("score", SolrQuery.ORDER.asc)//排序
                .setHighlight(true)//开启高亮
                .setHighlightSimplePre("<span style='color:red;'>")//高亮的开始标签
                .setHighlightSimplePost("</span>")//高亮的结束标签
                .addHighlightField("file_text")//高亮字段
                .set("start", 0)//开始条数
                .set("rows", 6)//每页显示条数
                .set("df", "file_text")//默认搜索域
                .set("fl", "*,score");//相应的结果中包含哪些域


        QueryResponse queryResponse = solrServer.query(solrQuery);
        SolrDocumentList results = queryResponse.getResults();//获取响应结果
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();//获取高亮的结果

        System.out.println("总记录数：" + results.getNumFound());
        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("file_author"));
            System.out.println(result.get("score"));

            Map<String, List<String>> listMap = highlighting.get(result.get("id"));
            if(listMap.containsKey("file_text")){
                System.out.println(listMap.get("file_text").get(0));
            }else{
                System.out.println(("file_text"));
            }
        }
    }
}
