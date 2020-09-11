package testsolr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.*;

public class synonymReadTest {

    //@Test
    //早期使用该方法进行测试，现在已经不使用该方法，转而使用sync()进行同步。
    public void synonymWord() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
        String user = "root";
        String password = "root";
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/searchengine?characterEncoding=utf8&useSSL=false", user, password);

        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM synonym");

//        File file = new File("D:\\NLZ_HD\\solrCloud\\solrhome01\\collection1\\newConf\\dynamicdic.txt");
//        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));

        while(resultSet.next()){
//            outputStreamWriter.write(resultSet.getString(1) + "\r\n");
//            打印词库内容到控制台
            System.out.println("basicQueryForList: " + resultSet.getString("extWord"));
        }
        System.out.println("同步词库");

//        outputStreamWriter.flush();
//        outputStreamWriter.close();

        resultSet.close();
        statement.close();
        conn.close();

    }
}
