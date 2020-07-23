package com.jindouyun.wx.controller;

import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunAddress;
import com.jindouyun.wx.annotation.LoginUser;
import com.jindouyun.wx.service.JindouyunAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName WxAddressController
 * @Description
 * @Author Bruce
 * @Date 2020/7/22 9:09 上午
 */
@RestController
@RequestMapping("/wx/address")
public class WxAddressController {

    @Autowired
    private JindouyunAddressService addressService;

    /**
     * @return java.lang.Object
     * @Description 用户收货地址列表
     * @Param [userId]
     **/
    @GetMapping("list")
    public Object list(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        List<JindouyunAddress> addresses = addressService.queryByUid(userId);
        return ResponseUtil.okList(addresses);
    }

    /**
     * @return java.lang.Object
     * @Description 收货地址详情
     * @Param [userId, id]
     **/
    @GetMapping("detail")
    public Object detail(@LoginUser Integer userId, @NotNull Integer id) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        JindouyunAddress address = addressService.query(userId, id);
        if (address == null) {
            return ResponseUtil.badArgumentValue();
        }
        return ResponseUtil.ok(address);
    }

    /**
     * 添加或更新收货地址
     *
     * @param userId  用户ID
     * @param address 用户收货地址
     * @return 添加或更新操作结果
     */
    @PostMapping("save")
    public Object save(@LoginUser Integer userId, @RequestBody JindouyunAddress address) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Object error = addressService.validate(address);
        if (error != null) {
            return error;
        }

        if (address.getIsDefault()) {
            // 重置其他收货地址的默认选项
            addressService.resetDefault(userId);
        }

        if (address.getId() == null || address.getId().equals(0)) {
            address.setId(null);
            address.setUserId(userId);
            addressService.add(address);
        } else {
            address.setUserId(userId);
            if (addressService.update(address) == 0) {
                return ResponseUtil.updatedDataFailed();
            }
        }
        return ResponseUtil.ok(address.getId());
    }

    /**
     * 删除收货地址
     *
     * @param userId  用户ID
     * @param address 用户收货地址，{ id: xxx }
     * @return 删除操作结果
     */
    @PostMapping("delete")
    public Object delete(@LoginUser Integer userId, @RequestBody JindouyunAddress address) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Integer id = address.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }

        addressService.delete(id);
        return ResponseUtil.ok();
    }

}
