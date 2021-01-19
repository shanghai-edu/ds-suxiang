package com.chineseall.authcenter.log.model;

import cn.sh.chineseall.framework.dao.core.CacheDimensionDocument;
import cn.sh.chineseall.framework.dao.core.annotation.*;
import cn.sh.chineseall.framework.dao.core.annotation.mongo.DocumentCollection;
import cn.sh.chineseall.framework.dao.core.annotation.mongo.DocumentDatabase;
import com.chineseall.authcenter.log.enums.LogType;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@DocumentConnection(configName = "mongo")
@DocumentDatabase(database = "splog")
@DocumentCollection(collection = "auth_log")
public class AuthLog  implements CacheDimensionDocument {

    @DocumentId
    private String id;

    @DocumentCreateTimestamp
    private Date createTime;

    @DocumentUpdateTimestamp
    private Date updateTime;

    @DocumentField
    private LogType logType; // 日志类型

    @DocumentField
    private Boolean loginSuccessFlag;

    @DocumentField
    private String clientId;

    @DocumentField
    private String clientName;

    @DocumentField
    private String idp;

    @DocumentField
    private String shibProvider;

    @DocumentField
    private String returnUrl; // 登录成功回调地址

    @DocumentField
    private Map<String, Object> userInfoMap;


    @Override
    public String[] generateCacheDimensions() {
        return new String[0];
    }
}
