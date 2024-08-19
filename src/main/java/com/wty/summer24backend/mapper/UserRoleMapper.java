package com.wty.summer24backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wty.summer24backend.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Select("<script>" +
            "SELECT user_id AS userId from user LEFT JOIN user_role ON user.id = user_role.user_id WHERE status != 0 " +
            "AND user_role.role_id IN " +
            "<foreach collection='roleIds' item='roleId' open='(' separator=',' close=')'>" +
            "#{roleId}" +
            "</foreach> " +
            "GROUP BY user_id " +
            "</script>"
            )
    List<Map<String, Object>> getUserIdsByRoleIds(@Param("roleIds") List<Integer> roleIds);
}
