package com.jindouyun.admin.util;

import com.jindouyun.admin.annotation.RequiresPermissionsDesc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private RequiresPermissions requiresPermissions;
    private RequiresPermissionsDesc requiresPermissionsDesc;
    private String api;

}
