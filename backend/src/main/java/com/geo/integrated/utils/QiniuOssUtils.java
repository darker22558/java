package com.geo.integrated.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author: whtli
 * @date: 2023/02/07
 * @description: 七牛云工具类，实现对象存储
 * AK等配置内容通过@Value()从application.yml中获取
 */
@Slf4j
@Component
public class QiniuOssUtils {
    /**
     * AK
     */
    @Value("${qiniu.accessKey}")
    private String accessKey;
    /**
     * SK
     */
    @Value("${qiniu.accessSecretKey}")
    private String accessSecretKey;
    /**
     * 存储空间名
     */
    @Value("${qiniu.bucketName}")
    private String bucketName;
    /**
     * 外链域名/路径
     */
    @Value("${qiniu.domainName}")
    private String domainName;

    /**
     * 上传图片到七牛云
     *
     * @param file 图片文件
     * @return 图片上传成功后的地址
     */
    public String uploadImage(MultipartFile file) {
        try {
            // 1.获取文件上传的流
            byte[] fileBytes = file.getBytes();

            // 2.创建日期目录分隔
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String datePath = dateFormat.format(new Date());

            // 3.获取文件名
            String originalFilename = file.getOriginalFilename();
            log.info("oss工具-图片开始上传-文件名 === {}", file.getOriginalFilename());
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = datePath + "/" + UUID.randomUUID().toString().replace("-", "") + suffix;

            // 4.构造一个带指定 Region 对象的配置类
            // Region.huabei()根据自己的对象空间的地址选
            Configuration cfg = new Configuration(Region.huabei());
            // 后来换了稳定的新加坡的空间并且绑定了个人域名没有做https的配置这时候要配置这个选项
            // 域名不支持https访问会报错ssl验证error
            // cfh.useHttpsDomains=false 关闭实列即可 默认是开启的
            UploadManager uploadManager = new UploadManager(cfg);

            // 5.获取七牛云提供的 token
            Auth auth = Auth.create(accessKey, accessSecretKey);
            String upToken = auth.uploadToken(bucketName);
            uploadManager.put(fileBytes, filename, upToken);

            // 6.上传成功返回地址
            String imageUrl = domainName + filename;
            log.info("oss工具-图片成功上传-地址为 === {}", imageUrl);
            return imageUrl;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除七牛云服务器中的图片
     *
     * @param url
     * @return true:删除成功; false:删除失败
     */
    public boolean delete(String url) {
        log.info("待删除的图片链接 === {}", url);
        //创建凭证
        Auth auth = Auth.create(accessKey, accessSecretKey);
        BucketManager bucketManager = new BucketManager(auth, new Configuration(Region.huabei()));
        // 此时pos为全路径，需要将外链域名去掉，七牛云的云端删除时只需要提供文件名即可
        // 截取最后一个指定字符串(此处为"/")之后的字符串。 此处fileName="9cb077ef572f49948f0dda60ed850a9d.jpg"
        String fileName = url.split(domainName)[1];
        log.info("待删除的文件名 === {}", fileName);
        try {
            bucketManager.delete(bucketName, fileName);
            log.info("图片删除成功 === {}", fileName);
            return true;
        } catch (QiniuException e) {
            // 如果遇到异常，说明删除失败
            log.error("图片删除失败 === 错误码：{}，错误信息：{}", e.code(), e.response);
        }
        return false;
    }
}
