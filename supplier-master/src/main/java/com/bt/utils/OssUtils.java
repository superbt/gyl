package com.bt.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.bt.constant.Constant;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 *
 * @website https://blog.csdn.net/Gaowumao
 * @Date 2022-05-14 11:40
 * @Description
 */
public class OssUtils {

    public static String uploadFileAvatar(MultipartFile file) {
        try{
            // 创建OSSClient实例。
           // OSS ossClient = new OSSClientBuilder().build(Constant.ENDPOINT,Constant.KEY_ID,Constant.KEY_SECRET);

            // 获取上传文件的输入流
            InputStream inputStream = file.getInputStream();

            //获取文件名称
            String filename = file.getOriginalFilename();

            //1、在文件名称里添加一个随机唯一的值
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            filename = uuid+filename;

            //2、把文件按照日期进行分类
            // 2021/7/17/xx.jpg
            //获取当前的日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            filename = datePath+"/"+filename;

            //调用OSS方法实现上传
            //第一个参数 Bucket名称
            //第二个参数  上传到OSS文件路径和文件名称
            //第三个参数  上传文件输入流
           // ossClient.putObject(Constant.BUCKET_NAME, filename, inputStream);

            // 关闭OSSClient。
            //ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            String url = "https://"+Constant.BUCKET_NAME+"."+Constant.ENDPOINT+"/"+filename;
            return url;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
