package testsolr;



import com.redgo.Application;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class TestSpringBootSolr {

    @Autowired
    private SolrClient solrClient;

    @Test
    public void test() throws IOException, SolrServerException {
        SolrQuery params = new SolrQuery("经验");
        params.set("df", "file_text");
        QueryResponse queryResponse = solrClient.query(params);


        SolrDocumentList results = queryResponse.getResults();
        System.out.println("总条数：" + results.getNumFound());

        for(SolrDocument result : results){
            System.out.println(result.get("id"));
            System.out.println(result.get("file_author"));
        }
    }
}
