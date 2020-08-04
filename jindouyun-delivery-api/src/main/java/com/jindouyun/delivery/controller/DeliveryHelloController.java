package com.jindouyun.delivery.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: DeliveryHelloController
 * @description: 测试类
 * @author: ZSZ
 * @date: 2020/8/4 13:59
 */
@RestController
@RequestMapping("/delivery")
public class DeliveryHelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
