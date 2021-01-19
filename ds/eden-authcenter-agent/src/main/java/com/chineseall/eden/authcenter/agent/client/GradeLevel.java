package com.chineseall.eden.authcenter.agent.client;

import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by yuchunlin on 2019/3/5.
 */
public enum GradeLevel {

    FIRST_GRADE("01", "一年级",1),
    SECOND_GRADE("02", "二年级",2),
    THIRD_GRADE("03", "三年级",3),
    FOURTH_GRADE("04", "四年级",4),
    FIFTH_GRADE("05", "五年级",5),
    SIXTH_GRADE("06", "六年级",6),
    SEVENTH_GRADE("07", "七年级",7),
    EIGHTH_GRADE("08", "八年级",8),
    NINTH_GRADE("09", "九年级",9),
    TENTH_GRADE("10", "高一",10),
    ELEVENTH_GRADE("11", "高二",11),
    TWELFTH_GRADE("12", "高三",12);

    @Getter
    private final String code;
    @Getter
    private final String description;

    @Getter
    private final int level;

    private final static Map<String, GradeLevel> gradeLevelMap;
    private final static Map<String, GradeLevel> descriptionMap;

    static {
        gradeLevelMap = new LinkedHashMap<>();
        descriptionMap = new HashMap<>();

        for (GradeLevel gradeLevel : GradeLevel.values()) {
            gradeLevelMap.put(gradeLevel.getCode(), gradeLevel);
            descriptionMap.put(gradeLevel.getDescription(), gradeLevel);
        }


    }

    GradeLevel(String code, String description, int level) {
        this.code = code;
        this.description = description;
        this.level = level;
    }


    public static GradeLevel getByName(String name) {
        try {
            return GradeLevel.valueOf(name);
        } catch (Exception ex) {
            return null;
        }
    }


    public static GradeLevel getByCode(String code) {
        try {
            return gradeLevelMap.get(code);
        } catch (Exception ex) {
            return null;
        }
    }

    public static GradeLevel getByDescription(String description) {
        return descriptionMap.get(description);
    }

    public GradeLevel nextGradeLevel(){
        GradeLevel[] values = GradeLevel.values();
        for (int i = 0; i < values.length; i++) {
            GradeLevel value = values[i];
            if (value == this){
                if (values.length - 1 == i){
                    return null;
                }
                return values[i + 1];
            }
        }
        return null;
    }

    /**
     * 级差
     * @param gradeLevel
     * @return
     */
    public int differLevel(GradeLevel gradeLevel){
        return gradeLevel.getLevel() - this.getLevel();
    }
}
