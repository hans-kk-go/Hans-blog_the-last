package com.hans.controller;


import com.hans.commen.ResponseResult;
import com.hans.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("menu")
    public ResponseResult getRouters(){
        return menuService.getRouters();
    }
}
