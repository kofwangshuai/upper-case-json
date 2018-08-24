package com.kof.json.entity;

import java.io.Serializable;

public class UserDto implements Serializable {

    private String UserId;
    private String Password;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
