package com.redgo.dao;


import org.junit.Test;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.sql.*;


//从mysql同步词库
@Repository
public class ThesaurusSync {

//@Test
    //早期使用该方法进行测试，现在已经不使用该方法，转而使用sync()进行同步。
    public void extWord() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
        String user = "root";
        String password = "root";
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/searchengine?characterEncoding=utf8&useSSL=false", user, password);

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT extWord FROM dynamicdic");

        File file = new File("D:\\NLZ_HD\\solrCloud\\solrhome01\\collection1\\newConf\\dynamicdic.txt");
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));

        while(resultSet.next()){
            outputStreamWriter.write(resultSet.getString(1) + "\r\n");
            //打印词库内容到控制台
            //System.out.println("basicQueryForList: " + resultSet.getString("extWord"));
        }
        System.out.println("同步词库");

        outputStreamWriter.flush();
        outputStreamWriter.close();

        resultSet.close();
        statement.close();
        conn.close();

    }

    public void sync(String sheet, String column, String path) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
        String user = "root";
        String password = "root";

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/searchengine?characterEncoding=utf8&useSSL=false", user, password);
        Statement statement = conn.createStatement();

        String sql = "SELECT " + column + " FROM " + sheet;
        ResultSet resultSet = statement.executeQuery(sql);

        File file = new File(path);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));

        while(resultSet.next()){
            outputStreamWriter.write(resultSet.getString(1) + "\r\n");
        }

        outputStreamWriter.flush();
        outputStreamWriter.close();

        resultSet.close();
        statement.close();
        conn.close();
        System.out.println("同步" + sheet + "词库");
    }
}
