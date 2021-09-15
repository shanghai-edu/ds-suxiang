package com.chineseall.eden.authcenter.agent.client;

import lombok.Getter;

/**
 * @author liuyp
 */
public enum ClientUserType {

    Student("1001","学生"),
    Teacher("2001","老师"),
    Mentor("2002","老师");

    @Getter
    private final String code;
    @Getter
    private final String description;

    ClientUserType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ClientUserType getValue(String name) {
        try {
            return ClientUserType.valueOf(name);
        } catch (Exception ex) {
            return null;
        }
    }
}
