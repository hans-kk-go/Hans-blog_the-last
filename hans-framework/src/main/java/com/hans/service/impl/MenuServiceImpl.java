package com.hans.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hans.commen.ResponseResult;
import com.hans.dao.MenuDao;
import com.hans.entity.Menu;
import com.hans.entity.Role;
import com.hans.entity.RoleMenu;
import com.hans.entity.UserRole;
import com.hans.service.MenuService;
import com.hans.service.RoleMenuService;
import com.hans.service.RoleService;
import com.hans.service.UserRoleService;
import com.hans.units.SecurityUtils;
import com.hans.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-04-01 23:39:18
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {
    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleMenuService roleMenuService;


    @Override
    public ResponseResult getRouters() {
        List<Menu> list2 = list();
        List<Menu> allRealMenu = list2.stream()
                .peek(one -> one.setChildren(list2.stream().filter(one1 -> one.getId().equals(one1.getParentId())).collect(Collectors.toList())))
                .collect(Collectors.toList());


        //获取当前登录的用户
        //根据用户id查询角色信息
        Long userId = SecurityUtils.getUserId();
        Optional<UserRole> user1 = userRoleService.list().stream()
                .filter(one -> one.getUserId().equals(userId))
                .findAny();

        Long roleId = user1.get().getRoleId();
        Role role = roleService.getById(roleId);
        String roleName = role.getRoleName();


        //根据角色id查询权限id
        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuLambdaQueryWrapper.eq(RoleMenu::getRoleId,roleId);
        List<RoleMenu> list = roleMenuService.list(roleMenuLambdaQueryWrapper);
        List<Long> MenuIdList = list.stream()
                .map(one -> one.getMenuId())
                .collect(Collectors.toList());

        //根据权限id查询权限信息
        List<Menu> menus = menuService.listByIds(MenuIdList);

        List<Menu> menuList = allRealMenu.stream()
                .filter(one -> menus.stream().map(one1 -> one1.getId()).collect(Collectors.toList()).contains(one.getId()))
                .collect(Collectors.toList());

        return ResponseResult.ok(menuList);
    }
}
