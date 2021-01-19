package com.chineseall.eden.authcenter.agent.oauth;

import lombok.Getter;

public enum OauthType {

    dianjiaoguan("dianjiaoguan","电教馆账号认证"),
    idp("idp","idp认证"),
    jsyd("jsyd","金山教育"),
    qpjy("qpjy","青浦教育"),
    hpjy("hpjy","黄浦教育"),
    fxjy("fxjy","奉贤教育"),
    pdjy("pdjy","浦东教育"),
    ypjy("ypjy","杨浦教育"),
    edenoperation("edenoperation","数字教材账号认证");


    @Getter
    private final String code;
    @Getter
    private final String description;

    OauthType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static OauthType getValue(String name) {
        try {
            return OauthType.valueOf(name);
        } catch (Exception ex) {
            return null;
        }
    }
}
