package com.hans.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hans.commen.ResponseResult;
import com.hans.entity.Menu;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-04-01 23:39:18
 */
public interface MenuService extends IService<Menu> {

    ResponseResult getRouters();
}

