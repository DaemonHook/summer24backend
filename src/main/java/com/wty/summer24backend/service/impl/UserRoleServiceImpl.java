package com.wty.summer24backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wty.summer24backend.entity.Role;
import com.wty.summer24backend.entity.UserRole;
import com.wty.summer24backend.mapper.RoleMapper;
import com.wty.summer24backend.mapper.UserRoleMapper;
import com.wty.summer24backend.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;

    @Autowired
    public UserRoleServiceImpl(RoleMapper roleMapper, UserRoleMapper userRoleMapper) {
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public void addUserRole(Long userId, List<Integer> roleIdList) {
        if (roleIdList == null || roleIdList.isEmpty()) {
            return;
        }

        List<Role> roleList = roleMapper.selectBatchIds(roleIdList);
        Set<Integer> roleIdSet = roleList
                .stream()
                .collect(HashSet::new, (set, role) -> set.add(role.getId()), HashSet::addAll);
        if (roleIdList.stream().anyMatch(roleId -> !roleIdSet.contains(roleId))) {
            throw new IllegalArgumentException("roleIdList中包含无效的roleId");
        }
        List<UserRole> userRoleList = new ArrayList<>();
        Date date = new Date();
        for (Integer roleId : roleIdList) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId).setRoleId(roleId).setCreateTime(date);
            userRoleList.add(userRole);
        }
        saveBatch(userRoleList);
    }

    @Override
    public void batchAddUserRole(List<Map<String, Object>> userRoleList) {
        if (userRoleList == null || userRoleList.isEmpty()) {
            return;
        }
        List<Role> allRoleList = roleMapper.selectList(new QueryWrapper<Role>());
        Set<Integer> allRoleIdSet = allRoleList.stream().map(Role::getId).collect(Collectors.toSet());
        Set<Integer> allRoleSet = allRoleList.stream().collect(
                HashSet::new,
                (set, role) -> set.add(role.getId()),
                HashSet::addAll);
        List<UserRole> userRoleToSaveList = new ArrayList<>();
        Date date = new Date();
        for (Map<String, Object> userRole : userRoleList) {
            Long userId = (Long) userRole.get("userId");
            if (userId == null) {
                continue;
            }
            @SuppressWarnings("unchecked")
            List<Integer> roleIdList = (List<Integer>) userRole.get("roleIdList");
            if (roleIdList != null) {
                roleIdList = roleIdList.stream().filter(allRoleIdSet::contains).collect(Collectors.toList());
            }
            if (roleIdList == null || roleIdList.isEmpty()) {
                continue;
            }
            for (Integer roleId : roleIdList) {
                UserRole userRoleToSave = new UserRole();
                userRoleToSave.setUserId(userId).setRoleId(roleId).setCreateTime(date);
                userRoleToSaveList.add(userRoleToSave);
            }
        }
        saveBatch(userRoleToSaveList);
    }

    @Override
    public List<Map<String, Object>> getUserIdsByRoleIds(List<Integer> roleIdList) {
        return Collections.emptyList();
    }
}
