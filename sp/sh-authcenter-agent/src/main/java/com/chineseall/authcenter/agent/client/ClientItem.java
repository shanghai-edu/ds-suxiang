package com.chineseall.authcenter.agent.client;

import com.chineseall.authcenter.agent.oauth.OauthType;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by yuchunlin on 2019/5/28.
 */
@Data
public class ClientItem implements Serializable{
    private String clientName;
    private String clientId;
    private String clientSecret;
    private String loginSuccessUrl;
    private String logoutSuccessUrl;

    private Integer loginWay; //登陆方式： 1-直接跳转无idp
    private OauthType loginGoal;
}
