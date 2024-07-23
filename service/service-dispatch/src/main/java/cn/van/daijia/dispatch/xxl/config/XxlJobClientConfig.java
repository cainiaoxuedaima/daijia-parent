package cn.van.daijia.dispatch.xxl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author：fan
 * @Description:
 * @DataTime:2024/7/23 17:44
 */
@Component
@Data
@ConfigurationProperties(prefix = "xxl.job.client")
public class XxlJobClientConfig {
    private Integer jobGroupId;
    private String addUrl;
    private String removeUrl;
    private String startJobUrl;
    private String stopJobUrl;
    private String addAndStartUrl;
}
