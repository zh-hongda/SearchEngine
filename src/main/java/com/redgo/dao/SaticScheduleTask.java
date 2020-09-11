package com.redgo.dao;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;

@Component
//定时更新词库，并上传配置到zookeeper，并reload zookeeper。
public class SaticScheduleTask {

    /*
    *添加定时任务
    *@Scheduled(cron = "0/5 * * * * ?")
    *或直接指定时间间隔，例如：5秒
    *@Scheduled(fixedRate=5000)
    */

    //同步词库
    @Scheduled(fixedRate=7000)
    public static void configureTasks2() throws SQLException, IOException, ClassNotFoundException {
        String extWordPath = "D:\\NLZ_HD\\solrCloud\\solrhome01\\collection1\\newConf\\dynamicdic.txt";
        String synonymWordPath = "D:\\NLZ_HD\\solrCloud\\solrhome01\\collection1\\newConf\\synonyms.txt";
        String stopWordPath = "D:\\NLZ_HD\\solrCloud\\solrhome01\\collection1\\newConf\\stopword.dic";

        ThesaurusSync thesaurusSync = new ThesaurusSync();
        thesaurusSync.sync("dynamicdic", "extWord", extWordPath);
        thesaurusSync.sync("stopword", "stopWord", stopWordPath);
    }

    //修改标志位
//    @Scheduled(fixedRate=7000)
//    public static void configureTasks1() {
//        String filePath = "D:\\NLZ_HD\\solrCloud\\solrhome01\\collection1\\newConf\\ik.conf"; // 文件路径
//        ChangeFlag obj = new ChangeFlag();
//        obj.write(filePath, obj.read(filePath)); // 读取修改文件
//    }

    //将配置文件上传到zookeeper
    @Scheduled(fixedRate=10000)
    public static void configureTasks3() throws SQLException, IOException, ClassNotFoundException {
        //修改标志位
        String filePath = "D:\\NLZ_HD\\solrCloud\\solrhome01\\collection1\\newConf\\ik.conf"; // 文件路径
        ChangeFlag changeFlag = new ChangeFlag();
        changeFlag.write(filePath, changeFlag.read(filePath));
        //上传并reload配置
        UploadToZookeeper uploadToZookeeper = new UploadToZookeeper();
        uploadToZookeeper.upload();
        uploadToZookeeper.reload();
    }
}
