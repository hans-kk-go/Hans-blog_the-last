package com.hans.controller;

import com.hans.commen.ResponseResult;
import com.hans.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @GetMapping("/userRole")
    public ResponseResult getuserRole(){
        return ResponseResult.ok(userRoleService.list());
    }
}
