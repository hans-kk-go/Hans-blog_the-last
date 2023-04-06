package com.hans.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hans.Constants.RedisConstants;
import com.hans.commen.ResponseResult;
import com.hans.dao.UserDao;
import com.hans.dto.UserDto;
import com.hans.entity.*;
import com.hans.securityAll.SecurityLoginUser;
import com.hans.service.*;
import com.hans.units.Jwt;
import com.hans.units.SecurityUtils;
import com.hans.vo.AdminUserInfo;
import com.hans.vo.LoginUserVo;
import com.hans.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-26 19:56:31
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleMenuService roleMenuService;


    //security新加代码
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public ResponseResult login(UserDto userDto) {


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDto.getUsername(),userDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        if (BeanUtil.isEmpty(authenticate)){
            return ResponseResult.fail(409,"用户名或密码错误");
        }

        SecurityLoginUser LogUser = (SecurityLoginUser)authenticate.getPrincipal();
        User user = LogUser.getUser();

        //存入redis
        String UserJson = JSONUtil.toJsonStr(user);
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGINID_KEY + user.getId(),UserJson);


        String token = Jwt.JwtCreate(user.getId());
        UserInfoVo userInfoVo = BeanUtil.toBean(user, UserInfoVo.class);
        LoginUserVo loginUserVo = new LoginUserVo(token, userInfoVo);
        return ResponseResult.ok("操作成功",loginUserVo);



    }

    @Override
    public ResponseResult logout() {
        //获取userid从securitycontext中
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id = user.getId();

        //删除redis中的对应数据
        Boolean delete = stringRedisTemplate.delete(RedisConstants.LOGINID_KEY + id);
        if(delete){
            return ResponseResult.ok("退出成功");
        }
        return ResponseResult.fail(430,"退出失败");


    }

    @Override
    public ResponseResult getUserInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        UserInfoVo userInfoVo = BeanUtil.toBean(user, UserInfoVo.class);
        return ResponseResult.ok(userInfoVo);
//        return ResponseResult.ok(list());
    }

    @Override
    public ResponseResult updateUserInfo(User userInfoVo) {
        updateById(userInfoVo);
        User byId = getById(userInfoVo.getId());
        return ResponseResult.ok(byId);
    }

    @Override
    public ResponseResult register(User user) {
        //1,判空
        if (StrUtil.isBlankIfStr(user)){
            return ResponseResult.fail(201,"值为空");
        }
        //2,判断是否存在
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserName,user.getUserName());
        if (!StrUtil.isBlankIfStr(getOne(userLambdaQueryWrapper))){
            return ResponseResult.fail(401,"用户已存在");
        }
        //3,加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        return ResponseResult.ok("注册成功");
    }

    @Override
    public ResponseResult adminLoginService(UserDto userDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (BeanUtil.isEmpty(authenticate)){
            return ResponseResult.fail(409,"用户名或者密码错误");
        }
        SecurityLoginUser securityLoginUser = (SecurityLoginUser)authenticate.getPrincipal();
        User user = securityLoginUser.getUser();

        //存入redis
        stringRedisTemplate.opsForValue().set(RedisConstants.ADMIN_LOGIN_ID_KEY+user.getId(), JSONUtil.toJsonStr(user));

        String token = Jwt.JwtCreate(user.getId());
//        LoginAdminUserVo loginAdminUserVo = new LoginAdminUserVo(token);

        HashMap<String, String> map = new HashMap<>();
        map.put("token",token);
        return ResponseResult.ok("操作成功", map);
    }

    @Override
    public ResponseResult getInfoAdmin() {
        //获取当前登录的用户
        //根据用户id查询角色信息
        List<String> roleKeys = new ArrayList<>();
        List<String> menuPersList = new ArrayList<>();
        Long userId = SecurityUtils.getUserId();
        if (userId == 1L){
            roleKeys.add("admin");
            menuPersList = menuService.list()
                    .stream().map(one -> one.getPerms()).collect(Collectors.toList());

        }else {
            List<UserRole> list1 = userRoleService.list();
            list1.stream()
                    .forEach(one -> System.out.println(one.toString()));

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
           menuPersList = menus.stream()
                    .map(one -> one.getPerms())
                    .collect(Collectors.toList());
        }



        //获取用户信息
        User user = getById(userId);
        UserInfoVo userInfoVo = BeanUtil.toBean(user, UserInfoVo.class);
        //封装数据返回

        AdminUserInfo adminUserInfo = new AdminUserInfo(menuPersList,roleKeys,userInfoVo);
        return ResponseResult.ok("操作成功",adminUserInfo);
    }




}
