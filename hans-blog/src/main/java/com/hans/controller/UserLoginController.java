package com.hans.controller;


import com.hans.commen.ResponseResult;
import com.hans.dto.UserDto;
import com.hans.entity.User;
import com.hans.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserLoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserDto userDto){
        return userService.login(userDto);
    }

    @PostMapping("/register")
    public ResponseResult login(@RequestBody User user){
        return userService.register(user);
    }


}
