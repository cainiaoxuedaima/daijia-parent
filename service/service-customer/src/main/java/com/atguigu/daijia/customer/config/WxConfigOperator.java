package com.atguigu.daijia.customer.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
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
    private WxMaService wxMaService(){
        //微信小程序id和密钥
        WxMaDefaultConfigImpl wxMaconfig = new WxMaDefaultConfigImpl();
        wxMaconfig.setAppid(wxConfigProperties.getAppId());
        wxMaconfig.setSecret(wxConfigProperties.getSecret());

        WxMaService service=new WxMaServiceImpl();
        service.setWxMaConfig(wxMaconfig);
        return service;
    }

}
