package com.chineseall.eden.authcenter.agent.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yuchunlin on 2019/2/12.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserType {
    STUDENT(2, "学生"),
    TEACHER(3, "老师"),
    MUNICIPAL_ADMIN(10, "市管理员"),
    ASSOCIATE_MUNICIPAL_ADMIN(11, "市级业务管理员"),
    COUNTY_ADMIN(20, "区县管理员"),
    ASSOCIATE_COUNTY_ADMIN(21, "区县业务管理员"),
    SCHOOL_ADMIN(30, "学校管理员"),
    ASSOCIATE_SCHOOL_ADMIN(31, "学校业务管理员"),
    OPERATING_ADMIN(1,"运营管理员"),
    UNKNOWN(99, "未指定");

    @Getter
    private final Integer code;
    @Getter
    private final String description;


    private final static Map<Integer, UserType> userTypeMap;
    private final static Map<String, UserType> descriptionMap;

    static {
        userTypeMap = Arrays.asList(values()).stream()
                .collect(Collectors.toMap(UserType::getCode, t -> t));
        descriptionMap = new HashMap<>();
        for (UserType userType : UserType.values()) {
            descriptionMap.put(userType.getDescription(), userType);
        }
    }
    public static UserType getByDescription(String description) {
        try {
            return descriptionMap.get(description);
        } catch (Exception ex) {
            return null;
        }
    }

    public static UserType safeParse(Integer value) {
        return safeParse(value, UNKNOWN);
    }

    public static UserType safeParse(Integer value, UserType defaultLevel) {
        if (value == null) return defaultLevel;
        return userTypeMap.getOrDefault(value, defaultLevel);
    }

    public static List<UserType> schoolAdmins() {
        return Arrays.asList(UserType.SCHOOL_ADMIN, UserType.ASSOCIATE_SCHOOL_ADMIN);
    }
}
