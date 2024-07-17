package cn.van.daijia.customer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author：fan
 * @Description:
 * @DataTime:2024/7/17 17:13
 */
@Component
@Data
@ConfigurationProperties(prefix="wx.miniapp")
public class WxConfigProperties {
    //微信小程序的id和密码
    private String appid;
    private String secret;
}
