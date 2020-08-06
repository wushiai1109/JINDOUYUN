package com.jindouyun.admin.controller;

import com.jindouyun.admin.service.LogHelper;
import com.jindouyun.admin.util.Permission;
import com.jindouyun.admin.util.PermissionUtil;
import com.jindouyun.common.util.IpUtil;
import com.jindouyun.common.util.JacksonUtil;
import com.jindouyun.core.util.ResponseUtil;
import com.jindouyun.db.domain.JindouyunAdmin;
import com.jindouyun.db.service.JindouyunAdminService;
import com.jindouyun.db.service.JindouyunPermissionService;
import com.jindouyun.db.service.JindouyunRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

import static com.jindouyun.admin.util.AdminResponseCode.ADMIN_INVALID_ACCOUNT;

@RestController
@RequestMapping("/admin/auth")
@Validated
@Api(value = "鉴权模块")
public class AdminAuthController {
    private final Log logger = LogFactory.getLog(AdminAuthController.class);

    @Autowired
    private JindouyunAdminService adminService;
    @Autowired
    private JindouyunRoleService roleService;
    @Autowired
    private JindouyunPermissionService permissionService;
    @Autowired
    private LogHelper logHelper;

    /*
     *  { username : value, password : value }
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录",notes = "用户登录接口",httpMethod = "POST")
    public Object login(@ApiParam(name = "body",value = "{\"username\":\"admin123\",\"password\":\"admin123\"}", required = true) @RequestBody String body,
                        HttpServletRequest request) {
        String username = JacksonUtil.parseString(body, "username");
        String password = JacksonUtil.parseString(body, "password");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResponseUtil.badArgument();
        }

        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(new UsernamePasswordToken(username, password));
        } catch (UnknownAccountException uae) {
            logHelper.logAuthFail("登录", "用户帐号或密码不正确");
            return ResponseUtil.fail(ADMIN_INVALID_ACCOUNT, "用户帐号或密码不正确");
        } catch (LockedAccountException lae) {
            logHelper.logAuthFail("登录", "用户帐号已锁定不可用");
            return ResponseUtil.fail(ADMIN_INVALID_ACCOUNT, "用户帐号已锁定不可用");

        } catch (AuthenticationException ae) {
            logHelper.logAuthFail("登录", "认证失败");
            return ResponseUtil.fail(ADMIN_INVALID_ACCOUNT, "认证失败");
        }

        currentUser = SecurityUtils.getSubject();
        JindouyunAdmin admin = (JindouyunAdmin) currentUser.getPrincipal();
        admin.setLastLoginIp(IpUtil.getIpAddr(request));
        admin.setLastLoginTime(LocalDateTime.now());
        adminService.updateById(admin);

        logHelper.logAuthSucceed("登录");

        // userInfo
        Map<String, Object> adminInfo = new HashMap<String, Object>();
        adminInfo.put("nickName", admin.getUsername());
        adminInfo.put("avatar", admin.getAvatar());

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", currentUser.getSession().getId());
        result.put("adminInfo", adminInfo);
        return ResponseUtil.ok(result);
    }


    @RequiresAuthentication
    @PostMapping("/logout")
    @ApiOperation(value = "注销",notes = "退出登录")
    public Object logout() {
        Subject currentUser = SecurityUtils.getSubject();

        logHelper.logAuthSucceed("退出");
        currentUser.logout();
        return ResponseUtil.ok();
    }


    @RequiresAuthentication
    @GetMapping("/info")
    @ApiOperation(value = "获取管理员信息",notes = "获取管理员，包括角色，权限，头像接口",httpMethod = "GET")
    public Object info() {
        Subject currentUser = SecurityUtils.getSubject();
        JindouyunAdmin admin = (JindouyunAdmin) currentUser.getPrincipal();

        Map<String, Object> data = new HashMap<>();
        data.put("name", admin.getUsername());
        data.put("avatar", admin.getAvatar());

        Integer[] roleIds = admin.getRoleIds();
        Set<String> roles = roleService.queryByIds(roleIds);
        Set<String> permissions = permissionService.queryByRoleIds(roleIds);
        data.put("roles", roles);
        // NOTE
        // 这里需要转换perms结构，因为对于前端而已API形式的权限更容易理解
        data.put("perms", toApi(permissions));
        return ResponseUtil.ok(data);
    }

    @Autowired
    private ApplicationContext context;
    private HashMap<String, String> systemPermissionsMap = null;

    private Collection<String> toApi(Set<String> permissions) {
        if (systemPermissionsMap == null) {
            systemPermissionsMap = new HashMap<>();
            final String basicPackage = "com.jindouyun.admin";
            List<Permission> systemPermissions = PermissionUtil.listPermission(context, basicPackage);
            for (Permission permission : systemPermissions) {
                String perm = permission.getRequiresPermissions().value()[0];
                String api = permission.getApi();
                systemPermissionsMap.put(perm, api);
            }
        }

        Collection<String> apis = new HashSet<>();
        for (String perm : permissions) {
            String api = systemPermissionsMap.get(perm);
            apis.add(api);

            if (perm.equals("*")) {
                apis.clear();
                apis.add("*");
                return apis;
                //                return systemPermissionsMap.values();

            }
        }
        return apis;
    }

    @GetMapping("/401")
    @ApiOperation(value = "未登录而访问失败跳转接口", notes = "前端不需要管，后端内部跳转接口",httpMethod = "GET")
    public Object page401() {
        return ResponseUtil.unlogin();
    }

    @GetMapping("/index")
    @ApiOperation(value = "登录成功跳转接口", notes = "前端不需要管，后端内部跳转接口",httpMethod = "GET")
    public Object pageIndex() {
        return ResponseUtil.ok();
    }

    @GetMapping("/403")
    @ApiOperation(value = "由于权限不足为拒绝操作", notes = "前端不需要管，后端内部跳转接口",httpMethod = "GET")
    public Object page403() {
        return ResponseUtil.unauthz();
    }
}
