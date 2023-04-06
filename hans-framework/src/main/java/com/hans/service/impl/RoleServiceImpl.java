package com.hans.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hans.dao.RoleDao;
import com.hans.entity.Role;
import com.hans.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-04-01 17:09:31
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

}
