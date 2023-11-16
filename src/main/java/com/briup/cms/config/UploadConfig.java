package com.briup.cms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*文件上传相关配置*/
@Data
@Component//作为部件装入IOC容器
@ConfigurationProperties(prefix = "oss")//就是导入的那个参数配置化包，可以把参数配置在application.yml里并导入对应属性的值(prefix是前缀)
public class UploadConfig {
    /**
     * OSS Access key
     */
    private String accessKey;
    /**
     * OSS Secret key
     */
    private String secretKey;
    /**
     * bucketName
     */
    private String bucket;
    /**
     * url地址,用于拼接 文件上传成功后回显的url
     */
    private String baseUrl;
}