package com.wty.summer24backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wty.summer24backend.entity.UserRole;

import java.util.List;
import java.util.Map;

public interface UserRoleService extends IService<UserRole> {
    void addUserRole(Long userId, List<Integer> roleIdList);

    void batchAddUserRole(List<Map<String, Object>> userRoleList);

    List<Map<String, Object>> getUserIdsByRoleIds(List<Integer> roleIdList);
}
