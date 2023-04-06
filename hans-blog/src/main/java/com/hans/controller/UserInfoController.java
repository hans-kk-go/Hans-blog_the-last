package com.hans.controller;


import com.hans.commen.ResponseResult;
import com.hans.entity.User;
import com.hans.service.UserService;
import com.hans.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private UserService userService;

    @GetMapping("/userinfo")
    public ResponseResult getUserInfo(){
        return userService.getUserInfo();
    }

    @PutMapping("/userinfo")
    public ResponseResult updateUserInfo(@RequestBody User userInfoVo){
        return userService.updateUserInfo(userInfoVo);
    }


}
