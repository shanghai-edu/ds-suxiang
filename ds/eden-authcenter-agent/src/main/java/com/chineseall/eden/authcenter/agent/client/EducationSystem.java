package com.chineseall.eden.authcenter.agent.client;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 学制
 * Created by yuchunlin on 2019/3/5.
 */
public enum EducationSystem {

    P5(new GradeLevel[]{
            GradeLevel.FIRST_GRADE,
            GradeLevel.SECOND_GRADE,
            GradeLevel.THIRD_GRADE,
            GradeLevel.FOURTH_GRADE,
            GradeLevel.FIFTH_GRADE,
    }, "小学五年制"),
    P6(new GradeLevel[]{
            GradeLevel.FIRST_GRADE,
            GradeLevel.SECOND_GRADE,
            GradeLevel.THIRD_GRADE,
            GradeLevel.FOURTH_GRADE,
            GradeLevel.FIFTH_GRADE,
            GradeLevel.SIXTH_GRADE,
    }, "小学六年制"),  // 小学六年制
    J3(new GradeLevel[]{
            GradeLevel.SEVENTH_GRADE,
            GradeLevel.EIGHTH_GRADE,
            GradeLevel.NINTH_GRADE,
    }, "初中三年制"),
    J4(new GradeLevel[]{
            GradeLevel.SIXTH_GRADE,
            GradeLevel.SEVENTH_GRADE,
            GradeLevel.EIGHTH_GRADE,
            GradeLevel.NINTH_GRADE,
    }, "初中四年制"),
    S3(new GradeLevel[]{
            GradeLevel.TENTH_GRADE,
            GradeLevel.ELEVENTH_GRADE,
            GradeLevel.TWELFTH_GRADE,
    }, "高中三年制"),

    J_S(new GradeLevel[]{
            GradeLevel.SIXTH_GRADE,
            GradeLevel.SEVENTH_GRADE,
            GradeLevel.EIGHTH_GRADE,
            GradeLevel.NINTH_GRADE,
            GradeLevel.TENTH_GRADE,
            GradeLevel.ELEVENTH_GRADE,
            GradeLevel.TWELFTH_GRADE,
    }, "完全中学"),
    P_J(new GradeLevel[]{
        GradeLevel.FIRST_GRADE,
        GradeLevel.SECOND_GRADE,
        GradeLevel.THIRD_GRADE,
        GradeLevel.FOURTH_GRADE,
        GradeLevel.FIFTH_GRADE,
        GradeLevel.SIXTH_GRADE,
        GradeLevel.SEVENTH_GRADE,
        GradeLevel.EIGHTH_GRADE,
        GradeLevel.NINTH_GRADE,
    }, "九年一贯制"),
    P_J_S(new GradeLevel[]{
            GradeLevel.FIRST_GRADE,
            GradeLevel.SECOND_GRADE,
            GradeLevel.THIRD_GRADE,
            GradeLevel.FOURTH_GRADE,
            GradeLevel.FIFTH_GRADE,
            GradeLevel.SIXTH_GRADE,
            GradeLevel.SEVENTH_GRADE,
            GradeLevel.EIGHTH_GRADE,
            GradeLevel.NINTH_GRADE,
            GradeLevel.TENTH_GRADE,
            GradeLevel.ELEVENTH_GRADE,
            GradeLevel.TWELFTH_GRADE,
    }, "十二年一贯制"),;

    @Getter
    private final GradeLevel[] gradeLevels;
    @Getter
    private final String description;

    private final static Map<String, EducationSystem> descriptionMap;


    EducationSystem(GradeLevel[] gradeLevels, String description) {
        this.gradeLevels = gradeLevels;
        this.description = description;
    }


    public static EducationSystem safeParse(String name) {
        try {
            return EducationSystem.valueOf(name);
        } catch (Exception ignore) {
            return null;
        }
    }

    static {
        descriptionMap = new HashMap<>();
        for (EducationSystem educationSystem : EducationSystem.values()) {
            descriptionMap.put(educationSystem.getDescription(), educationSystem);
        }
    }

    public static EducationSystem getByDescription(String description) {
        try {
            return descriptionMap.get(description);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 学制的下一个年级
     * @param gradeLevel
     * @return
     */
    public GradeLevel nextGradeLevel(GradeLevel gradeLevel){
        GradeLevel[] values = this.getGradeLevels();
        for (int i = 0; i < values.length; i++) {
            GradeLevel value = values[i];
            if (value == gradeLevel){
                if (values.length - 1 == i){
                    return null;
                }
                return values[i + 1];
            }
        }
        return null;
    }
}
