package com.chineseall.eden.authcenter.agent.client;


import lombok.Data;

/**
 * @author zhangjiaquan
 */
@Data
public class AuthUserInfo {

    private String id;//uuid
    private String realName;//姓名
    private UserType userType;//教师、学生
    private String source;//来源
    /**
     * 学生
     */
    private String studentId;//学生id
    private String studentCode;//学籍号
    private EducationSystem studentStage;//学段
    private GradeLevel studentGrade;//年级
    private String studentClass;//班级
    private String studentStageName;//学段名称
    private String studentGradeName;//年级名称
    private String studentClassName;//班级名称
    private String studentSchoolId;//学籍学校id
    private String studentCodeStatus;//学籍当前状态
    private String studentSchoolCode;//学籍学校代码
    private String studentSchoolName;//学籍学校名称
    private String studentRegionCode;//学籍区县代码
    private String studentRegionName;//学籍区县名称
    /**
     * 教师
     */
    private String teacherTrainingNo;//师训号
    private String teacherCertificateNo;
    private String teacherTrainingUserName;//师训用户名
    private String teacherRegionCode;//师训区县id
    private String teacherCampusCode;//师训学区id
    private String teacherTrainingSchoolType;//师训学校类型
    private EducationSystem teacherStage;//师训学段id
    private GradeLevel teacherGrade;//师训年级id
    private SubjectType teacherSubject;//师训专业
    private String teacherSchoolId;//师训学校id
    private String teacherSchoolName;//师训学校名称

}
