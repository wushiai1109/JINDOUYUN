package com.jindouyun.core.storage.config;

import com.jindouyun.core.storage.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageAutoConfiguration {

    private final StorageProperties properties;

    public StorageAutoConfiguration(StorageProperties properties) {
        this.properties = properties;
    }

    @Bean
    public StorageService storageService() {
        StorageService storageService = new StorageService();
        String active = this.properties.getActive();
        storageService.setActive(active);
        if (active.equals("local")) {
            storageService.setStorage(localStorage());
        } else if (active.equals("minio")) {
            storageService.setStorage(minioStorage());
        } else {
            throw new RuntimeException("当前存储模式 " + active + " 不支持");
        }

        return storageService;
    }

    @Bean
    public LocalStorage localStorage() {
        LocalStorage localStorage = new LocalStorage();
        StorageProperties.Local local = this.properties.getLocal();
        localStorage.setAddress(local.getAddress());
        localStorage.setStoragePath(local.getStoragePath());
        return localStorage;
    }


    @Bean
    public MinIOStorage minioStorage() {
        MinIOStorage minIOStorage = new MinIOStorage();
        StorageProperties.MinIO minIO = this.properties.getMinIO();
        minIOStorage.setAccessKey(minIO.getAccessKey());
        minIOStorage.setSecretKey(minIO.getSecretKey());
        minIOStorage.setBucketName(minIO.getBucketName());
        minIOStorage.setEndpoint(minIO.getEndpoint());
        return minIOStorage;
    }
}
