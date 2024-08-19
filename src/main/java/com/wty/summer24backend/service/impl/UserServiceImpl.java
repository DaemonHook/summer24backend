package com.wty.summer24backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wty.summer24backend.entity.User;
import com.wty.summer24backend.mapper.UserMapper;
import com.wty.summer24backend.service.UserRoleService;
import com.wty.summer24backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final UserRoleService userRoleService;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserRoleService userRoleService) {
        this.userMapper = userMapper;
        this.userRoleService = userRoleService;
    }

    @Override
    public boolean existsByUserName(String userName, boolean throwExceptionWhenExists) throws RuntimeException {
        User user = userMapper.selectOne(
                new QueryWrapper<>().eq("user_name", userName.trim()).ne("status", 0));
        if (throwExceptionWhenExists && user != null) {
            throw new RuntimeException();
        } else {
            return false;
        }
    }

    @Override
    public Map<String, Boolean> batchCheckUserName(List<String> userNames) {
        if (userNames == null) {
            return Collections.emptyMap();
        }
        List<User> existUsers = userMapper.selectList(
                new QueryWrapper<User>().in("user_name", userNames).ne("status", 0));
        Set<String> existUserNameSet = existUsers.stream().map(user -> user.getUsername().trim()).collect(Collectors.toSet());
        Map<String, Boolean> result = new HashMap<>();
        for (String name : userNames) {
            result.put(name, existUserNameSet.contains(name));
        }
        return result;
    }
}
