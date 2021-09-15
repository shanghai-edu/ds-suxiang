package com.chineseall.authcenter.agent.client;


import com.chineseall.authcenter.agent.oauth.OauthType;
import lombok.Data;

@Data
public class ClientDataInfo {

    private AuthUserInfo authUserInfo;

    private String returnUrl;

    private OauthType oauthType;

}
