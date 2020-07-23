package com.jindouyun.core.storage;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.MinioException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @className:
 * @description:
 * @author: ZSZ
 * @date: 2020/4/20 21:32
 */


public class MinIOStorage implements Storage {

    private final Log logger = LogFactory.getLog(MinIOStorage.class);

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * 获取OSS客户端对象
     *
     * @return ossClient
     */
    private MinioClient getOSSClient() throws InvalidPortException, InvalidEndpointException {
        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        return new MinioClient(endpoint, accessKey, secretKey);
    }

    private String getBaseUrl(){
        return endpoint+"/"+bucketName+"/";
    }

    @Override
    public void store(InputStream inputStream, long contentLength, String contentType, String keyName) {
        try {
            MinioClient minioClient = getOSSClient();
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if(isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket(bucketName);
            }

            // 使用putObject上传一个文件到存储桶中。
            //putObject(String bucketName, String objectName, InputStream stream, long size, String contentType)
            minioClient.putObject(bucketName,keyName, inputStream,contentLength,contentType);
        } catch(MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (Exception e){
            System.out.println("OOS Upload Failed,Error occurred: " + e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String keyName) {
        return null;
    }

    @Override
    public Resource loadAsResource(String keyName) {
        try {
            URL url = new URL(generateUrl(keyName));
            Resource resource = new UrlResource(url);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else{
                return null;
            }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void delete(String keyName) {
        try {
            getOSSClient().removeObject(bucketName,keyName);
        } catch (Exception e) {
            System.out.println("OOS DELETE Failed,Error occurred: " + e);
        }
    }

    @Override
    public String generateUrl(String keyName) {
        return getBaseUrl() + keyName;
    }
}
