package com.wty.summer24backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wty.summer24backend.VO.ResponseVO;
import com.wty.summer24backend.common.enums.CommonStatusEnum;
import com.wty.summer24backend.common.enums.StatusEnum;
import com.wty.summer24backend.dto.RoleDTO;
import com.wty.summer24backend.entity.Permission;
import com.wty.summer24backend.entity.Role;
import com.wty.summer24backend.entity.RolePermission;
import com.wty.summer24backend.service.PermissionService;
import com.wty.summer24backend.service.RolePermissionService;
import com.wty.summer24backend.service.RoleService;
import com.wty.summer24backend.service.UserRoleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(tags = "role-api")
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final RolePermissionService rolePermissionService;
    private final PermissionService permissionService;

    @Autowired
    public RoleController(RoleService roleService, UserRoleService userRoleService, RolePermissionService rolePermissionService, PermissionService permissionService) {
        this.roleService = roleService;
        this.userRoleService = userRoleService;
        this.rolePermissionService = rolePermissionService;
        this.permissionService = permissionService;
    }

    @ApiOperation(value = "添加角色")
    @PostMapping
    public ResponseVO<String> addRole(@RequestBody RoleDTO roleInfo) {
        try {
            roleService.existsByName(roleInfo.getName(), true);
        } catch (RuntimeException e) {
            return ResponseVO.error("角色已存在");
        }
        roleService.save(roleInfo.toEntity(Role.class).setStatus(Role.Status.ENABLE).setCreateTime(new Date()).setUpdateTime(new Date()));
        return ResponseVO.success();
    }

    @ApiOperation("检查角色是否存在")
    @GetMapping("/name/exists")
    public ResponseVO<Boolean> existsByName(@RequestParam @ApiParam(value = "角色名", required = true) String name) {
        try {
            return ResponseVO.success(roleService.existsByName(name, false));
        } catch (RuntimeException e) {
            return ResponseVO.error();
        }
    }

    @ApiOperation(value = "获取角色列表")
    @GetMapping
    public ResponseVO<List<Role>> getRoleList(@RequestParam(defaultValue = "") @ApiParam("搜索内容") String searchContent) {
        List<Role> roleList = roleService.list(new QueryWrapper<Role>().eq("status", 1).and(wrapper -> wrapper.like("name", searchContent).or().like("description", searchContent)).orderByDesc("create_time"));
        return ResponseVO.success(roleList);
    }

    @ApiOperation(value = "修改角色信息")
    @PutMapping
    public ResponseVO<String> updateRole(@RequestBody RoleDTO roleInfo) {
        Role role = roleService.getById(roleInfo.getId());
        if (role == null || !role.getStatus().equals(Role.Status.ENABLE)) {
            return ResponseVO.error(StatusEnum.ROLE_NOT_FOUND);
        }
        try {
            roleService.existsByName(roleInfo.getName(), true);
        } catch (RuntimeException e) {
            return ResponseVO.error();
        }
        if (roleInfo.isNotModified(role)) {
            return ResponseVO.error(StatusEnum.ROLE_INFO_NOT_CHANGED);
        }
        roleInfo.copyDataTo(role);
        role.setUpdateTime(new Date());
        roleService.updateById(role);
        return ResponseVO.success();
    }

    @ApiOperation(value = "批量删除角色")
    @DeleteMapping
    public ResponseVO<String> deleteRoles(@RequestParam List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseVO.error(CommonStatusEnum.BAD_REQUEST);
        }
        List<Map<String, Object>> userIdList = userRoleService.getUserIdsByRoleIds(ids);
        if (userIdList != null && !userIdList.isEmpty()) {
            return ResponseVO.error(StatusEnum.ROLE_IN_USE);
        }
        // 采用逻辑删除
        List<Role> roleList = (List<Role>) roleService.listByIds(ids);
        roleList.forEach(role -> role.setStatus(Role.Status.DELETED));
        roleService.updateBatchById(roleList);
        return ResponseVO.success();
    }

    /**
     * 添加角色权限
     *
     * @param roleId        角色id
     * @param permissionIds 权限id列表
     * @param deleteOld     是否删除原有权限
     */
    @ApiOperation(value = "添加角色权限")
    @PostMapping("/{roleId}/permissions")
    @Transactional(rollbackFor = Exception.class)
    public ResponseVO<String> addRolePermissions(@PathVariable("roleId") @ApiParam(value = "角色id", required = true) Integer roleId, @RequestParam @ApiParam(value = "权限id列表", required = true) List<Integer> permissionIds, @RequestParam @ApiParam(value = "是否删除原有权限", required = true) boolean deleteOld) {
        Role role = roleService.getById(roleId);
        if (role == null || !role.getStatus().equals(Role.Status.ENABLE)) {
            return ResponseVO.error(StatusEnum.ROLE_NOT_FOUND);
        }
        if (permissionIds == null || permissionIds.isEmpty()) {
            return ResponseVO.error(StatusEnum.PERMISSION_NOT_EMPTY);
        }
        List<Permission> permissionList = permissionService.list(new QueryWrapper<Permission>().in("id", permissionIds));
        Set<Integer> permissionIdSet = permissionList.stream().map(Permission::getId).collect(Collectors.toSet());
        if (permissionIds.stream().anyMatch(permissionId -> !permissionIdSet.contains(permissionId))) {
            return ResponseVO.error(StatusEnum.PERMISSION_NOT_FOUND);
        }
        List<Integer> oldPermissionIdList = new ArrayList<>();
        if (deleteOld) {
            rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("role_id", roleId));
        } else {
            oldPermissionIdList = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id", roleId)).stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        }
        List<RolePermission> rolePermissionList = new ArrayList<>();
        for (int permissionId : permissionIds) {
            if (oldPermissionIdList.contains(permissionId)) {
                continue;
            }
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId).setPermissionId(permissionId);
            rolePermissionList.add(rolePermission);
        }
        rolePermissionService.saveBatch(rolePermissionList);
        return ResponseVO.success();
    }

    @ApiOperation(value = "删除角色权限")
    @DeleteMapping("role-permissions")
    public ResponseVO<String> deleteRolePermissions(@RequestParam List<Integer> rolePermissionIds) {
        if (rolePermissionIds != null && !rolePermissionIds.isEmpty()) {
            rolePermissionService.removeByIds(rolePermissionIds);
        }
        return ResponseVO.success();
    }
}
