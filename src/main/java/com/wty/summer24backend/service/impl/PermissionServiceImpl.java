package com.wty.summer24backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wty.summer24backend.entity.Permission;
import com.wty.summer24backend.mapper.PermissionMapper;
import com.wty.summer24backend.service.PermissionService;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
