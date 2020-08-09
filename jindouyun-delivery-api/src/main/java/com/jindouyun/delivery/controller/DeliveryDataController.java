package com.jindouyun.delivery.controller;

import com.jindouyun.delivery.service.JindouyunClockinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DeliveryDataController
 * @Description
 * @Author Bruce
 * @Date 2020/8/9 3:07 下午
 */
@RestController
@RequestMapping("/delivery/data")
@CrossOrigin(origins = "*",maxAge = 3600)
public class DeliveryDataController {

    @Autowired
    private JindouyunClockinService clockinService;

    




}
