package com.wty.summer24backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wty.summer24backend.entity.Permission;
import com.wty.summer24backend.entity.RolePermission;
import com.wty.summer24backend.mapper.RolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
        implements com.wty.summer24backend.service.RolePermissionService {

    private final RolePermissionMapper rolePermissionMapper;

    @Autowired
    public RolePermissionServiceImpl(RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionMapper = rolePermissionMapper;
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Integer roleId) {
        return rolePermissionMapper.getPermissionsByRoleId(roleId);
    }
}
