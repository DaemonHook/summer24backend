package com.wty.summer24backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wty.summer24backend.entity.Role;
import com.wty.summer24backend.mapper.RoleMapper;
import com.wty.summer24backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public boolean existsByName(String roleName, boolean throwExceptionWhenExists) throws RuntimeException {
        Role role = roleMapper.selectOne(new QueryWrapper<Role>().eq("name", roleName.trim())
                .eq("status", 1));
        if (role != null && throwExceptionWhenExists) {
            throw new RuntimeException();
        }
        return role != null;
    }
}
