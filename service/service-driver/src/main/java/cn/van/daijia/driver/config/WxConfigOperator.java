package cn.van.daijia.driver.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author：fan
 * @Description:
 * @DataTime:2024/7/17 17:16
 */
@Component
public class WxConfigOperator {
    @Autowired
    private WxConfigProperties wxConfigProperties;
    @Bean
    public WxMaService wxMaService(){
        //微信小程序id和密钥
        WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
        wxMaConfig.setAppid(wxConfigProperties.getAppid());
        wxMaConfig.setSecret(wxConfigProperties.getSecret());

        WxMaService service=new WxMaServiceImpl();
        service.setWxMaConfig(wxMaConfig);
        return service;
    }

}
