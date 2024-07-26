package cn.van.daijia.driver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：fan
 * @Description:
 * @DataTime:2024/7/26 21:12
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties {
    private String endpointUrl;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
