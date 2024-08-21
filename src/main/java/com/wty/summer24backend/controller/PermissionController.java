package com.wty.summer24backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wty.summer24backend.service.PermissionService;
import com.wty.summer24backend.entity.Permission;
import com.wty.summer24backend.common.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "permission-api")
@RequestMapping("/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 获取权限列表
     */
    @GetMapping
    @ApiOperation(value="获取权限列表")
    public ResponseVO<List<Permission>> getPermissionList() {
        List<Permission> permissionList = permissionService.list(new QueryWrapper<>());
        return ResponseVO.success(permissionList);
    }
}
