package cn.van.daijia.driver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author：fan
 * @Description:
 * @DataTime:2024/7/18 22:00
 */
@Data
@Component
@ConfigurationProperties(prefix = "tencent.cloud")
public class TencentCloudProperties {
     private String secretId;
     private String secretKey;
     private String region;
     private String bucketPrivate;

     private String personGroupId;//人脸组id
}
