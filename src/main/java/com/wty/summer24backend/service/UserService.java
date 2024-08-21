package com.wty.summer24backend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wty.summer24backend.common.exceptions.CustomRuntimeException;
import com.wty.summer24backend.dto.UserDTO;
import com.wty.summer24backend.entity.User;
import org.springframework.data.domain.Page;

import java.util.*;

public interface UserService extends IService<User> {

    List<Map<String, Object>> getUserRoleAndPermissionsByUserId(List<Long> userIds);

    Page<User> getUserList(String userName, String minCreateTime, String maxCreateTime, String orderBy, String orderMethod, Integer page, Integer pageSize);

    User addOneUser(UserDTO userDTO) throws CustomRuntimeException, CustomRuntimeException;

    Map<String, Object> batchCreateUser(List<UserDTO> userDTOS);

    /**
     * 判断用户名是否存在
     *
     * @param userName                 用户名
     * @param throwExceptionWhenExists 什么情况下抛出异常
     *                                 null: 不抛出异常, true: 用户名存在时抛出异常, false: 用户名不存在时抛出异常
     */
    boolean existsByUserName(String userName, Boolean throwExceptionWhenExists) throws CustomRuntimeException;

    Map<String, Boolean> batchCheckUserName(List<String> userNames);

}
