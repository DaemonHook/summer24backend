package com.wty.summer24backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String avatarPath;
    private String phone;
    private String email;
    // 0：男 1：女
    private Integer gender;
    private String address;
    private String introduction;
    private String trueName;
    // 状态：0：禁用，1：启用，2：删除
    private Integer status;
    private Date createTime;
    private Date updateTime;

    @TableField(exist = false)
    private List<String> roleList;

    @TableField(exist = false)
    private List<String> permissionList;

    public static final String ORDER_FIELDS = "create_time,user_name,true_name,email,gender,address,introduction,phone," +
            "status,update_time";

    public static final List<String> ORDER_FIELD_LIST = Arrays.asList(ORDER_FIELDS.split(","));
}
