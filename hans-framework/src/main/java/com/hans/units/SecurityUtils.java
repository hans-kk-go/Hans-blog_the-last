package com.hans.units;

import com.hans.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils
{

    /**
     *
     * 获取用户
     **/
    public static User getUser()
    {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

//    public static Boolean isAdmin(){
//        Long id = getLoginUser().getUser().getId();
//        return id != null && 1L == id;
//    }

    public static Long getUserId() {
        return getUser().getId();
    }
}
