package com.chineseall.eden.authcenter.agent.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public enum DianjiaoguanStudentStageType {


    P5("1", "小学"),
    J4("2", "初中"),
    S3("3", "高中");

    @Getter
    private final String code;
    @Getter
    private final String description;



    private final static Map<String, DianjiaoguanStudentStageType> stageMap;
    private final static Map<String, DianjiaoguanStudentStageType> descriptionMap;

    static {
        stageMap = new LinkedHashMap<>();
        descriptionMap = new HashMap<>();

        for (DianjiaoguanStudentStageType type : DianjiaoguanStudentStageType.values()) {
            stageMap.put(type.getCode(), type);
            descriptionMap.put(type.getDescription(), type);
        }


    }

    DianjiaoguanStudentStageType(String code, String description) {
        this.code = code;
        this.description = description;
    }


    public static DianjiaoguanStudentStageType getByName(String name) {
        try {
            return DianjiaoguanStudentStageType.valueOf(name);
        } catch (Exception ex) {
            return null;
        }
    }


    public static DianjiaoguanStudentStageType getByCode(String code) {
        try {
            return stageMap.get(code);
        } catch (Exception ex) {
            return null;
        }
    }

    public static DianjiaoguanStudentStageType getByDescription(String description) {
        try {
            return descriptionMap.get(description);
        } catch (Exception ex) {
            return null;
        }
    }

}
