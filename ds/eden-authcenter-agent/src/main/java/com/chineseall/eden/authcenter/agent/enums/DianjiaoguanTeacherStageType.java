package com.chineseall.eden.authcenter.agent.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public enum DianjiaoguanTeacherStageType {


    P5("103", "小学"),
    J4("101", "初中"),
    S3("102", "高中");

    @Getter
    private final String code;
    @Getter
    private final String description;



    private final static Map<String, DianjiaoguanTeacherStageType> stageMap;
    private final static Map<String, DianjiaoguanTeacherStageType> descriptionMap;

    static {
        stageMap = new LinkedHashMap<>();
        descriptionMap = new HashMap<>();

        for (DianjiaoguanTeacherStageType type : DianjiaoguanTeacherStageType.values()) {
            stageMap.put(type.getCode(), type);
            descriptionMap.put(type.getDescription(), type);
        }


    }

    DianjiaoguanTeacherStageType(String code, String description) {
        this.code = code;
        this.description = description;
    }


    public static DianjiaoguanTeacherStageType getByName(String name) {
        try {
            return DianjiaoguanTeacherStageType.valueOf(name);
        } catch (Exception ex) {
            return null;
        }
    }


    public static DianjiaoguanTeacherStageType getByCode(String code) {
        try {
            return stageMap.get(code);
        } catch (Exception ex) {
            return null;
        }
    }

    public static DianjiaoguanTeacherStageType getByDescription(String description) {
        try {
            return descriptionMap.get(description);
        } catch (Exception ex) {
            return null;
        }
    }

}
