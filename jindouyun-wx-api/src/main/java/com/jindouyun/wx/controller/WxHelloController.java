package com.jindouyun.wx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: HelloController
 * @description:
 * @author: ZSZ
 * @date: 2020/7/20 19:52
 */
@RestController
@RequestMapping("/wx")
public class WxHelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
