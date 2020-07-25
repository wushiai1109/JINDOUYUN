package com.jindouyun.wx.controller;

import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.service.JindouyunOrderService;
import com.jindouyun.wx.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WxUserController
 * @Description 用户服务
 * @Author Bruce
 * @Date 2020/7/25 3:40 下午
 */
@RestController
@RequestMapping("/wx/user")
@CrossOrigin(origins = "*",maxAge = 3600)
public class WxUserController {

    @Autowired
    private JindouyunOrderService orderService;

    /**
     * 用户个人页面数据
     * <p>
     * 目前是用户订单统计信息
     *
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("index")
    public Object list(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        Map<Object, Object> data = new HashMap<>();
        data.put("order", orderService.orderInfo(userId));
        return ResponseUtil.ok(data);
    }
    
}
