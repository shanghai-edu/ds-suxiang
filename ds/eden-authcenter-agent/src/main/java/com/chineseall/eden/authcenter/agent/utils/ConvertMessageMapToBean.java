package com.chineseall.eden.authcenter.agent.utils;

import cn.sh.chineseall.framework.core.util.StringUtils;
import com.chineseall.eden.authcenter.agent.client.*;
import com.chineseall.eden.authcenter.agent.enums.DianjiaoguanGradeType;
import com.chineseall.eden.authcenter.agent.enums.DianjiaoguanStudentStageType;
import com.chineseall.eden.authcenter.agent.enums.DianjiaoguanSubjectType;
import com.chineseall.eden.authcenter.agent.enums.DianjiaoguanTeacherStageType;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConvertMessageMapToBean {


    public static AuthUserInfo converDianjiaoguanData(Map<String, Object> dataMap){
        List<Map<String, String>> attributes = (List<Map<String, String>>) dataMap.get("attributes");
        Map<String, String> map = new LinkedHashMap<>();
        if(null != attributes) {
            attributes.stream().forEach(stringMap -> {
                for (Map.Entry entry : stringMap.entrySet()) {
                    map.put(entry.getKey().toString(), entry.getValue().toString());
                }
            });
        }
        AuthUserInfo authUserInfo = new AuthUserInfo();
        if(StringUtils.isNotBlank(map.get("ID")))
            authUserInfo.setId(map.get("ID").toString());
        if(StringUtils.isNotBlank(map.get("XM")))
            authUserInfo.setRealName(map.get("XM").toString());
        if(StringUtils.isNotBlank(map.get("ROLE"))) {
            if (map.get("ROLE").toString().equals("1001"))
                authUserInfo.setUserType(UserType.STUDENT);
            else if(map.get("ROLE").toString().equals("2001")||map.get("ROLE").toString().equals("2002"))
                authUserInfo.setUserType(UserType.TEACHER);
        }
        if(StringUtils.isNotBlank(map.get("XJXSID")))
            authUserInfo.setStudentId(map.get("XJXSID").toString());
        if(StringUtils.isNotBlank(map.get("XJFH")))
            authUserInfo.setStudentCode(map.get("XJFH").toString());
        if(StringUtils.isNotBlank(map.get("XJXD"))) {
            DianjiaoguanStudentStageType type = DianjiaoguanStudentStageType.getByCode(map.get("XJXD").toString());
            if(type != null)
                authUserInfo.setStudentStage(EducationSystem.safeParse(type.name()));
        }
        if(StringUtils.isNotBlank(map.get("XJNJ"))){
            DianjiaoguanGradeType type = DianjiaoguanGradeType.getByCode(map.get("XJNJ").toString());
            if(type != null)
                authUserInfo.setStudentGrade(GradeLevel.getByName(type.name()));
        }
        if(StringUtils.isNotBlank(map.get("XJBJ")))
            authUserInfo.setStudentClass(map.get("XJBJ").toString());
        if(authUserInfo.getStudentStage() != null)
            authUserInfo.setStudentStageName(authUserInfo.getStudentStage().getDescription());
        if(authUserInfo.getStudentGrade() != null)
            authUserInfo.setStudentGradeName(authUserInfo.getStudentGrade().getDescription());
        if(StringUtils.isNotBlank(map.get("XJBJMC")))
            authUserInfo.setStudentClassName(map.get("XJBJMC").toString());
        if(StringUtils.isNotBlank(map.get("XJXXID")))
            authUserInfo.setStudentSchoolId(map.get("XJXXID").toString());
        if(StringUtils.isNotBlank(map.get("XJDQZT")))
            authUserInfo.setStudentCodeStatus(map.get("XJDQZT").toString());
        if(StringUtils.isNotBlank(map.get("XJXXDM")))
            authUserInfo.setStudentSchoolCode(map.get("XJXXDM").toString());
        if(StringUtils.isNotBlank(map.get("XJXXMC")))
            authUserInfo.setStudentSchoolName(map.get("XJXXMC").toString());
        if(StringUtils.isNotBlank(map.get("XJQXDM")))
            authUserInfo.setStudentRegionCode(map.get("XJQXDM").toString());
        if(StringUtils.isNotBlank(map.get("XJQXMC")))
            authUserInfo.setStudentRegionName(map.get("XJQXMC").toString());
        if(StringUtils.isNotBlank(map.get("SXTEACHNUMBER")))
            authUserInfo.setTeacherTrainingNo(map.get("SXTEACHNUMBER").toString());
        if(StringUtils.isNotBlank(map.get("SXENAME")))
            authUserInfo.setTeacherTrainingUserName(map.get("SXENAME").toString());
        if(StringUtils.isNotBlank(map.get("SXQUXIANID")))
            authUserInfo.setTeacherRegionCode(map.get("SXQUXIANID").toString());
        if(StringUtils.isNotBlank(map.get("SXXUQUID")))
            authUserInfo.setTeacherCampusCode(map.get("SXXUQUID").toString());
        if(StringUtils.isNotBlank(map.get("SXSCHOOL_TYPEID")))
            authUserInfo.setTeacherTrainingSchoolType(map.get("SXSCHOOL_TYPEID").toString());
        if(StringUtils.isNotBlank(map.get("SXXUEDUANID"))){
            DianjiaoguanTeacherStageType type = DianjiaoguanTeacherStageType.getByCode(map.get("SXXUEDUANID").toString());
            if(type != null)
                authUserInfo.setTeacherStage(EducationSystem.safeParse(type.name()));
        }
        if(StringUtils.isNotBlank(map.get("SXNIANJIID"))){
            DianjiaoguanGradeType type = DianjiaoguanGradeType.getByCode(map.get("SXNIANJIID").toString());
            if(type != null)
                authUserInfo.setTeacherGrade(GradeLevel.getByName(type.name()));
        }
        if(StringUtils.isNotBlank(map.get("SXMASTERSUBJECT"))) {
            DianjiaoguanSubjectType dType = DianjiaoguanSubjectType.getByCode(Integer.valueOf(map.get("SXMASTERSUBJECT").toString()));
            if(null != dType)
                authUserInfo.setTeacherSubject(SubjectType.getByCode(dType.getOperationCode()));
        }
        if(StringUtils.isNotBlank(map.get("SXSCHOOLID")))
            authUserInfo.setTeacherSchoolId(map.get("SXSCHOOLID").toString());
        if(StringUtils.isNotBlank(map.get("SXSCHOOL_NAME")))
            authUserInfo.setTeacherSchoolName(map.get("SXSCHOOL_NAME").toString());
        return authUserInfo;
    }



    public static AuthUserInfo converEdenoperationData(Map<String, Object> dataMap){
        Map<String, String> map = (Map<String, String>) dataMap.get("authUserInfo");
        AuthUserInfo authUserInfo = new AuthUserInfo();
        if(StringUtils.isNotBlank(map.get("id")))
            authUserInfo.setId(map.get("id").toString());
        if(StringUtils.isNotBlank(map.get("real_name")))
            authUserInfo.setRealName(map.get("real_name").toString());
        if(StringUtils.isNotBlank(map.get("user_type")))
            authUserInfo.setUserType(UserType.valueOf(map.get("user_type")));
        if(StringUtils.isNotBlank(map.get("student_id")))
            authUserInfo.setStudentId(map.get("student_id").toString());
        if(StringUtils.isNotBlank(map.get("student_code")))
            authUserInfo.setStudentCode(map.get("student_code").toString());
        if(StringUtils.isNotBlank(map.get("student_stage")))
            authUserInfo.setStudentStage(EducationSystem.safeParse(map.get("student_stage").toString()));
        if(StringUtils.isNotBlank(map.get("student_grade")))
            authUserInfo.setStudentGrade(GradeLevel.getByName(map.get("student_grade")));
        if(StringUtils.isNotBlank(map.get("student_class")))
            authUserInfo.setStudentClass(map.get("student_class").toString());
        if(StringUtils.isNotBlank(map.get("student_stage_name")))
            authUserInfo.setStudentStageName(map.get("student_stage_name").toString());
        if(authUserInfo.getStudentGrade() != null)
            authUserInfo.setStudentGradeName(authUserInfo.getStudentGrade().getDescription());
        if(StringUtils.isNotBlank(map.get("student_class_name")))
            authUserInfo.setStudentClassName(map.get("student_class_name").toString());
        if(StringUtils.isNotBlank(map.get("student_school_id")))
            authUserInfo.setStudentSchoolId(map.get("student_school_id").toString());
        if(StringUtils.isNotBlank(map.get("student_code_status")))
            authUserInfo.setStudentCodeStatus(map.get("student_code_status").toString());
        if(StringUtils.isNotBlank(map.get("student_school_code")))
            authUserInfo.setStudentSchoolCode(map.get("student_school_code").toString());
        if(StringUtils.isNotBlank(map.get("student_school_name")))
            authUserInfo.setStudentSchoolName(map.get("student_school_name").toString());
        if(StringUtils.isNotBlank(map.get("student_region_code")))
            authUserInfo.setStudentRegionCode(map.get("student_region_code").toString());
        if(StringUtils.isNotBlank(map.get("student_region_name")))
            authUserInfo.setStudentRegionName(map.get("student_region_name").toString());
        if(StringUtils.isNotBlank(map.get("teacher_training_no")))
            authUserInfo.setTeacherTrainingNo(map.get("teacher_training_no").toString());
        if(StringUtils.isNotBlank(map.get("teacher_training_user_name")))
            authUserInfo.setTeacherTrainingUserName(map.get("teacher_training_user_name").toString());
        if(StringUtils.isNotBlank(map.get("teacher_region_code")))
            authUserInfo.setTeacherRegionCode(map.get("teacher_region_code").toString());
        if(StringUtils.isNotBlank(map.get("teacher_campus_code")))
            authUserInfo.setTeacherCampusCode(map.get("teacher_campus_code").toString());
        if(StringUtils.isNotBlank(map.get("teacher_training_school_type")))
            authUserInfo.setTeacherTrainingSchoolType(map.get("teacher_training_school_type").toString());
        if(StringUtils.isNotBlank(map.get("teacher_stage")))
            authUserInfo.setTeacherStage(EducationSystem.safeParse(map.get("teacher_stage").toString()));
        if(StringUtils.isNotBlank(map.get("teacher_grade")))
            authUserInfo.setTeacherGrade(GradeLevel.getByName(map.get("teacher_grade")));
        if(StringUtils.isNotBlank(map.get("teacher_subject")))
            authUserInfo.setTeacherSubject(SubjectType.getByName(map.get("teacher_subject")));
        if(StringUtils.isNotBlank(map.get("teacher_school_id")))
            authUserInfo.setTeacherSchoolId(map.get("teacher_school_id").toString());
        if(StringUtils.isNotBlank(map.get("teacher_school_name")))
            authUserInfo.setTeacherSchoolName(map.get("teacher_school_name").toString());
        return authUserInfo;
    }



}
