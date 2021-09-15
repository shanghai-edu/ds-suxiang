package com.chineseall.eden.authcenter.agent.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public enum DianjiaoguanGradeType {

    FIRST_GRADE("11", "一年级",1),
    SECOND_GRADE("12", "二年级",2),
    THIRD_GRADE("13", "三年级",3),
    FOURTH_GRADE("14", "四年级",4),
    FIFTH_GRADE("15", "五年级",5),
    SIXTH_GRADE("16", "六年级",6),
    SEVENTH_GRADE("17", "七年级",7),
    EIGHTH_GRADE("18", "八年级",8),
    NINTH_GRADE("19", "九年级",9),
    TENTH_GRADE("31", "高一",10),
    ELEVENTH_GRADE("32", "高二",11),
    TWELFTH_GRADE("33", "高三",12);

    @Getter
    private final String code;
    @Getter
    private final String description;

    @Getter
    private final int level;

    private final static Map<String, DianjiaoguanGradeType> gradeLevelMap;
    private final static Map<String, DianjiaoguanGradeType> descriptionMap;

    static {
        gradeLevelMap = new LinkedHashMap<>();
        descriptionMap = new HashMap<>();

        for (DianjiaoguanGradeType type : DianjiaoguanGradeType.values()) {
            gradeLevelMap.put(type.getCode(), type);
            descriptionMap.put(type.getDescription(), type);
        }


    }

    DianjiaoguanGradeType(String code, String description, int level) {
        this.code = code;
        this.description = description;
        this.level = level;
    }


    public static DianjiaoguanGradeType getByName(String name) {
        try {
            return DianjiaoguanGradeType.valueOf(name);
        } catch (Exception ex) {
            return null;
        }
    }


    public static DianjiaoguanGradeType getByCode(String code) {
        try {
            return gradeLevelMap.get(code);
        } catch (Exception ex) {
            return null;
        }
    }

}
