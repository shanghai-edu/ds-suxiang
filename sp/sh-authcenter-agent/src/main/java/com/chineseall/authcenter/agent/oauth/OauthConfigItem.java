package com.chineseall.authcenter.agent.oauth;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by yuchunlin on 2019/1/3.
 */
@Data
public class OauthConfigItem implements Serializable {
    //http://castest.edu.sh.cn/CAS/oauth2.0
    private String oauthUrl;

    private String clientId;

    private String clientSecret;

    private String loginSuccessUrl;

    private String logoutSuccessUrl;
}
