package com.redgo.dao;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

//上传配置到zookeeper 并reload
public class UploadToZookeeper {
    private int state;

    @Test
    public void upload() throws IOException {

        String scriptPath = "D:\\NLZ_HD\\solrCloud\\uploadToZookeeper.bat";
        Process process = Runtime.getRuntime().exec(scriptPath);
        System.out.println("上传配置到zookeeper");

        //将cmd的执行结果通过java的IO流输出到IDE的控制台
//        InputStream is = process.getInputStream();
//        InputStreamReader isr = new InputStreamReader(is,"GBK");
//        BufferedReader br = new BufferedReader(isr);
//        String content = br.readLine();
//        while (content != null) {
//            System.out.println(content);
//            content = br.readLine();
//        }
    }

    @Test
    public void reload() throws IOException {
        URL U = new URL("http://localhost:8180/solr/admin/collections?action=RELOAD&name=core2");

        //url对象用openconnection()打开连接；获得URLConnection类对象，
        //再用URLConnection类对象的connect（）方法进行连接
        HttpURLConnection connection = (HttpURLConnection) U.openConnection();
        connection.connect();

        state = connection.getResponseCode();//获取连接状态
        if(state==200) {
            System.out.println("重载zookeeper配置");
            System.out.println("----------------------------");
        }
        else {
            System.out.println("重载zookeeper配置失败");
            System.out.println("----------------------------");
        }

        //输出响应内容
//        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        String line;
//        while ((line = in.readLine())!= null)
//        {
//            System.out.println(line);
//        }
//        in.close();
    }
}
