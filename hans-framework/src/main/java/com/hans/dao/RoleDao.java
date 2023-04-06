package com.hans.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hans.entity.Role;
import org.apache.ibatis.annotations.Mapper;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-01 17:09:29
 */
@Mapper
public interface RoleDao extends BaseMapper<Role> {

}

