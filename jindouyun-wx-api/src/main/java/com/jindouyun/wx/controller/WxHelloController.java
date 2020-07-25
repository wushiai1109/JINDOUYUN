package com.jindouyun.wx.controller;

import com.jindouyun.core.util.ResponseUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "*", maxAge = 3600)
public class WxHelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    /**
     * 测试数据
     *
     * @return 测试数据
     */
    @RequestMapping("/index")
    public Object index() {
        return ResponseUtil.ok("hello world, this is wx service");
    }

}
