package com.jindouyun.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: HelloController
 * @description:
 * @author: ZSZ
 * @date: 2020/7/20 19:50
 */
@RestController
@RequestMapping("/admin")
public class AdminHelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

}
