package com.chineseall.eden.authcenter.agent.client;

import com.chineseall.eden.authcenter.agent.oauth.OauthType;
import lombok.Data;

@Data
public class ClientDataInfo {

    private AuthUserInfo authUserInfo;

    private String returnUrl;

    private OauthType oauthType;

}
