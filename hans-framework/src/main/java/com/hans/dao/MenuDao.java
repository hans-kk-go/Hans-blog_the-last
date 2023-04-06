package com.hans.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hans.entity.Menu;
import org.apache.ibatis.annotations.Mapper;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-01 23:39:18
 */
@Mapper
public interface MenuDao extends BaseMapper<Menu> {

}

