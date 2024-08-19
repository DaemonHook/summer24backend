package com.wty.summer24backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wty.summer24backend.entity.Permission;
import com.wty.summer24backend.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
    @Select("SELECT p.* FROM permission AS p " +
            "LEFT JOIN role_permission AS rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    List<Permission> getPermissionsByRoleId(Integer roleId);
}
