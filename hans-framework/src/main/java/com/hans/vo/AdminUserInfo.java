package com.hans.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserInfo {
    private Object permissions;
    private Object roles;
    private Object user;
}
