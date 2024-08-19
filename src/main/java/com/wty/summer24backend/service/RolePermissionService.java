package com.wty.summer24backend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wty.summer24backend.entity.Permission;
import com.wty.summer24backend.entity.RolePermission;

import java.util.List;

public interface RolePermissionService extends IService<RolePermission> {
    List<Permission> getPermissionsByRoleId(Integer roleId);
}
