package com.wty.summer24backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wty.summer24backend.entity.Permission;

import java.util.List;

public interface PermissionService extends IService<Permission> {

    List<com.wty.summer24backend.controller.Permission> list(QueryWrapper<com.wty.summer24backend.controller.Permission> permissionQueryWrapper);
}
