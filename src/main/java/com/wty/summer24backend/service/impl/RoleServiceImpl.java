package com.wty.summer24backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wty.summer24backend.common.enums.StatusEnum;
import com.wty.summer24backend.common.exceptions.CustomRuntimeException;
import com.wty.summer24backend.entity.Role;
import com.wty.summer24backend.mapper.RoleMapper;
import com.wty.summer24backend.service.RoleService;
import com.wty.summer24backend.util.DataUtils;
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
    public boolean existsByName(String roleName, boolean throwExceptionWhenExists) throws CustomRuntimeException {
        if (DataUtils.checkEmptyString(roleName)) {
            throw new CustomRuntimeException(StatusEnum.ROLE_NAME_NOT_EMPTY);
        }
        Role role = roleMapper.selectOne(new QueryWrapper<Role>().eq("name", roleName.trim()).eq("status", Role.Status.ENABLE));
        if (role != null && throwExceptionWhenExists) {
            throw new CustomRuntimeException(StatusEnum.ROLE_NAME_EXISTS);
        }
        return role != null;
    }

}
