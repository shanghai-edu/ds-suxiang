package com.chineseall.authcenter.log.enums;

import lombok.Getter;

public enum LogType {

    login("login","登录操作"),
    logout("logout","登出操作");


    @Getter
    private final String code;
    @Getter
    private final String description;

    LogType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static LogType getValue(String name) {
        try {
            return LogType.valueOf(name);
        } catch (Exception ex) {
            return null;
        }
    }
}
