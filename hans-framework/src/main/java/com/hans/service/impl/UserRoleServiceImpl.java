package com.hans.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hans.dao.UserRoleDao;
import com.hans.entity.UserRole;
import com.hans.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-04-02 00:25:30
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements UserRoleService {

}
