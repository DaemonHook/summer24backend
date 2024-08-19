package com.wty.summer24backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wty.summer24backend.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    List<Map<String, Object>> getUserRoleAndPermissionByUserId(List<Long> userId);

    Page<User> getUserList(String userName, String minCreateTime, String maxCreateTime, String orderBy,
                           String orderMethod, Integer page, Integer pageSize);

    User addOneUser(User user) throws RuntimeException;

    Map<String, Object> batchCreateUser(List<User> users);

    boolean existsByUserName(String userName, boolean throwExceptWhenExist) throws RuntimeException;

    Map<String, Boolean> batchCheckUserName(List<String> userNames);
 }
