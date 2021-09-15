package com.chineseall.eden.authcenter.agent.client;

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
}
