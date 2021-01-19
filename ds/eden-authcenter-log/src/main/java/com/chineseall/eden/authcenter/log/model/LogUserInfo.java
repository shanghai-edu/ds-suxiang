package com.chineseall.eden.authcenter.log.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class LogUserInfo implements Serializable {

    private String userId;

    private String realName;

    private String userType;

    private String schoolId;

    private String schoolName;

    private String teacherTrainingNo;

    private String studentCode;


}
