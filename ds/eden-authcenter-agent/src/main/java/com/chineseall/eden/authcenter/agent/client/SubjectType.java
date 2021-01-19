package com.chineseall.eden.authcenter.agent.client;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;


public enum SubjectType {
    SX("SX", "数学", 1),
    YW("YW", "语文", 2),
    YY("YY", "英语", 3),
    WL("WL", "物理", 4),
    SW("SW", "生物", 5),
    HX("HX", "化学", 6),
    LS("LS", "历史", 7),
    DL("DL", "地理", 8),
    ZZ("ZZ", "思想政治", 9),
    PD("PD", "思想品德", 10),
    PS("PS", "品德与社会", 11),
    LD("LD", "劳动技术", 12),
    XX("XX", "信息科技", 13),
    TY("TY", "体育", 14),
    SM("SM", "生命科学", 15),
    KJ("KJ", "科学与技术", 16),
    MS("MS", "美术", 17),
    ZR("ZR", "自然", 18),
    YU("YU", "音乐", 19),
    TJ("TJ", "体育与健身", 20),
    FZ("FZ", "法制", 21),
    KX("KX", "科学", 22),
    YS("YS", "艺术", 23),
    ZJ("ZJ", "专题教育", 24),
    ZH("ZH", "综合", 25),
    SH("SH", "社会", 26),
    DF("DF", "道德与法治", 27),
    TS("TS", "通用技术", 28),
    QT("QT", "其它", 29),
    ;

    private final static Map<String, SubjectType> subjectTypeMap;
    private final static Map<String, SubjectType> descriptionMap;


    static {
        subjectTypeMap = new LinkedHashMap<>();
        descriptionMap = new HashMap<>();
        for (SubjectType subjectType : SubjectType.values()) {
            subjectTypeMap.put(subjectType.getCode(), subjectType);
            descriptionMap.put(subjectType.getDescription(), subjectType);
        }
    }

    @Getter
    private final String code;
    @Getter
    private final String description;
    @Getter
    private final int sort;

    SubjectType(String code, String description, int sort) {
        this.code = code;
        this.description = description;
        this.sort = sort;
    }

    public static SubjectType getByName(String name) {
        try {
            return SubjectType.valueOf(name);
        } catch (Exception ex) {
            return null;
        }
    }

    public static SubjectType getByCode(String code) {
        try {
            return subjectTypeMap.get(code);
        } catch (Exception ex) {
            return null;
        }
    }

    public static SubjectType getByDescription(String description) {
        try {
            return descriptionMap.get(description);
        } catch (Exception ex) {
            return null;
        }
    }

    public static List<Map<String, Object>> getAll() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            List<SubjectType> list = descriptionMap.values().stream().sorted(Comparator.comparing(SubjectType::getSort)).collect(Collectors.toList());
            list.forEach(subjectType -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("code", subjectType.getCode());
                map.put("name", subjectType.getDescription());
                map.put("sort", subjectType.getSort());
                result.add(map);
            });
            return result;
        } catch (Exception ex) {
            return null;
        }
    }
}
