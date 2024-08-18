package com.wty.summer24backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wty.summer24backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
