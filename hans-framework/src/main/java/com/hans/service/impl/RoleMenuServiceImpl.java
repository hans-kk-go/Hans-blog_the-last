package com.hans.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hans.dao.RoleMenuDao;
import com.hans.entity.RoleMenu;
import com.hans.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-04-02 00:24:52
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuDao, RoleMenu> implements RoleMenuService {

}
