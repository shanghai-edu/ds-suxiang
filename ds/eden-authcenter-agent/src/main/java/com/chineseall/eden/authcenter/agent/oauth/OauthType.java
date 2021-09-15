package com.chineseall.eden.authcenter.agent.oauth;

import lombok.Getter;

import java.util.List;

public enum OauthType {

    dianjiaoguan("dianjiaoguan","电教馆账号认证","oauth"),

    edenoperation("edenoperation","数字教材账号认证","oauth"),
    qpjy("qpjy","青浦教育","idp"),
    xhjy("xhjy","徐汇教育","idp"),
    cnjy("cnjy","长宁教育","idp"),
    jajy("jajy","静安教育","idp"),
    ptjy("ptjy","普陀教育","idp"),
    hkjy("hkjy","虹口教育","idp"),
    mhjy("mhjy","闵行教育","idp"),
    bsjy("bsjy","宝山教育","idp"),
    jdjy("jdjy","嘉定教育","idp"),
    jsjy("jsjy","金山教育","idp"),
    sjjy("sjjy","松江教育","idp"),
    fxjy("fxjy","奉贤教育","idp"),
    cmjy("cmjy","崇明教育","idp"),

    hpjy("hpjy","黄浦教育","idp"),
    pdjy("pdjy","浦东教育","idp"),
    ypjy("ypjy","杨浦教育","idp");


    @Getter
    private final String code;
    @Getter
    private final String description;


    @Getter
    private final String type;



    OauthType(String code, String description,String type) {
        this.code = code;
        this.description = description;
        this.type = type;
    }

    public static OauthType getValue(String name) {
        try {
            return OauthType.valueOf(name);
        } catch (Exception ex) {
            return null;
        }
    }
}
