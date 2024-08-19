package com.wty.summer24backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wty.summer24backend.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    /**
     * 判断用户名是否存在
     * @param userName  用户名
     */
    boolean existsByUserName(String userName, boolean throwExceptWhenExist) throws RuntimeException;

    Map<String, Boolean> batchCheckUserName(List<String> userNames);
 }
