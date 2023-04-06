package com.hans.controller;


import com.hans.commen.ResponseResult;
import com.hans.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class UserInfoController {

    @Autowired
    private UserService userService;

    @GetMapping("/getInfo")
    public ResponseResult getInfo(){

        return userService.getInfoAdmin();
    }
}
