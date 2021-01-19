package com.chineseall.eden.authcenter.agent.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yuchunlin on 2019/5/28.
 */

@Data
@Component
@ConfigurationProperties(prefix = "oauthclient")
public class OauthClient {

    private List<ClientItem> clients;


}
