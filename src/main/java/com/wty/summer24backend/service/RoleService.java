package com.wty.summer24backend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wty.summer24backend.entity.Role;

public interface RoleService extends IService<Role> {
    boolean existsByName(String name, boolean throwExceptionWhenExists) throws RuntimeException;
}
