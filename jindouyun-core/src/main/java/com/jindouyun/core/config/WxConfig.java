package com.jindouyun.core.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

@Configuration
public class WxConfig {
    @Autowired
    private WxProperties properties;

    @Autowired
    private DeliveryWxProperties deliveryWxProperties;

    @Bean(name = "wxMaConfig")
    public WxMaConfig wxMaConfig() {
        WxMaInMemoryConfig config = new WxMaInMemoryConfig();
        config.setAppid(properties.getAppId());
        config.setSecret(properties.getAppSecret());
        return config;
    }

    @Bean(name = "deliveryWxMaConfig")
    public WxMaConfig deliveryWxMaConfig(){
        WxMaInMemoryConfig config = new WxMaInMemoryConfig();
        config.setAppid(deliveryWxProperties.getAppId());
        config.setSecret(deliveryWxProperties.getAppSecret());
        return config;
    }


    @Bean(name = "wxMaService")
    public WxMaService wxMaService(@Qualifier("wxMaConfig") WxMaConfig maConfig) {
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(maConfig);
        return service;
    }

    @Bean(name = "deliveryWxMaService")
    public WxMaService deliveryWxMaService(@Qualifier("deliveryWxMaConfig") WxMaConfig maConfig){
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(maConfig);
        return service;
    }

    @Bean(name = "wxPayConfig")
    public WxPayConfig wxPayConfig() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(properties.getAppId());
        payConfig.setMchId(properties.getMchId());
        payConfig.setMchKey(properties.getMchKey());
        payConfig.setNotifyUrl(properties.getNotifyUrl());
        payConfig.setKeyPath(properties.getKeyPath());
        payConfig.setTradeType("JSAPI");
        payConfig.setSignType("MD5");
        return payConfig;
    }


    @Bean(name = "wxPayService")
    public WxPayService wxPayService(WxPayConfig payConfig) {
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}