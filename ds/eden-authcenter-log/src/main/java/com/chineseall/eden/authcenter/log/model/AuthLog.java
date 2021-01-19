package com.chineseall.eden.authcenter.log.model;

import cn.sh.chineseall.framework.dao.core.CacheDimensionDocument;
import cn.sh.chineseall.framework.dao.core.annotation.*;
import cn.sh.chineseall.framework.dao.core.annotation.mongo.DocumentCollection;
import cn.sh.chineseall.framework.dao.core.annotation.mongo.DocumentDatabase;
import com.chineseall.eden.authcenter.log.enums.LogType;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@DocumentConnection(configName = "mongo")
@DocumentDatabase(database = "dslog")
@DocumentCollection(collection = "auth_log")
public class AuthLog  implements CacheDimensionDocument {

    @DocumentId
    private String id;

    @DocumentCreateTimestamp
    private Date createTime;

    @DocumentUpdateTimestamp
    private Date updateTime;

    @DocumentField
    private String clientId;

    @DocumentField
    private LogType logType; // 日志类型

    @DocumentField
    private String authSource; // 请求发起方

    @DocumentField
    private String fowardUrl; // 登录跳转地址

    @DocumentField
    private String oauthType; // 第三方登录类型

    @DocumentField
    private Boolean loginSuccessFlag; // 是否回调成功

    @DocumentField
    private String returnUrl; // 登录成功回调地址

    @DocumentField
    private String finalReturnUrl; // 登录或登出成功后最终回调地址

    @DocumentField
    private Boolean getUserInfoSuccessFlag; // 获取用户信息是否成功

    @DocumentField
    private LogUserInfo logUserInfo; // 登录用户信息

    @DocumentField
    private Map<String, Object> userInfoMap;

    @DocumentField
    private String browser;

    @DocumentField
    private String version;

    @DocumentField
    private String os;

    @DocumentField
    private String device;

    @Override
    public String[] generateCacheDimensions() {
        return new String[0];
    }
}
