package com.redgo.dao;

import java.io.*;
import java.util.Date;

//修改标志位，使词库发生变化时，动态生效
public class ChangeFlag {
    /**
     * 字符串+1方法，该方法将其结尾的整数+1,适用于任何以整数结尾的字符串,不限格式，不限分隔符。
     * @author zxcvbnmzb
     * @param testStr 要+1的字符串
     * @return +1后的字符串
     * @exception NumberFormatException
     */
    //以前，IK分词器动态更新时，需要通过标志位+1来触发。
    //现在将标志位改为时间戳，所有addOne()方法现在并未使用。
    public static String addOne(String testStr) {
        String[] strs = testStr.split("[^0-9]");//根据不是数字的字符拆分字符串
        String numStr = strs[strs.length - 1];//取出最后一组数字
        if (numStr != null && numStr.length() > 0) {//如果最后一组没有数字(也就是不以数字结尾)，抛NumberFormatException异常
            int n = numStr.length();//取出字符串的长度
            int num = Integer.parseInt(numStr) + 1;//将该数字加一
            String added = String.valueOf(num);
            n = Math.min(n, added.length());
            //拼接字符串
            return testStr.subSequence(0, testStr.length() - n) + added;
        } else {
            throw new NumberFormatException();
        }
    }

    /**
     * 读取文件内容
     *
     * @param filePath
     * @return
     */
    public String read(String filePath) {
        BufferedReader br = null;
        String line = null;
        StringBuffer buf = new StringBuffer();

        Date date = new Date();
        long timestamp = date.getTime();

        try {
            // 根据文件路径创建缓冲输入流
            br = new BufferedReader(new FileReader(filePath));

            // 循环读取文件的每一行, 对需要修改的行进行修改, 放入缓冲对象中
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                // 此处根据实际需要修改某些行的内容
                if (line.startsWith("lastupdate")) {
                    line="lastupdate=" + timestamp;
                    buf.append(line);
                }
                // 如果不用修改, 则按原来的内容回写
                else {
                    buf.append(line);
                }
                buf.append(System.getProperty("line.separator"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                }
            }
        }
        return buf.toString();
    }

    /**
     * 将内容回写到文件中
     *
     * @param filePath
     * @param content
     */
    public void write(String filePath, String content) {
        BufferedWriter bw = null;

        try {
            // 根据文件路径创建缓冲输出流
            bw = new BufferedWriter(new FileWriter(filePath));
            // 将内容写入文件中
            bw.write(content);
            System.out.println("修改标志位");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    bw = null;
                }
            }
        }
    }

    /**
     * 主方法
     */
//    public static void main(String[] args) {
//        String filePath = "C:/Users/hongda/Desktop/ik.conf"; // 文件路径
//        AddOne obj = new AddOne();
//        obj.write(filePath, obj.read(filePath)); // 读取修改文件
//    }
}
