package com.chineseall.eden.authcenter.agent.client;

import cn.sh.chineseall.framework.core.util.CollectionUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Created by yuchunlin on 2019/5/28.
 */

@Data
@Component
@ConfigurationProperties(prefix = "oauthclient")
public class OauthClient {

    private List<ClientItem> clients;


    public ClientItem getClientItem(String clientId){
        if (CollectionUtils.isNotEmpty(clients)){
            for (ClientItem item : clients) {
                if (Objects.equals(item.getClientId(),clientId)){
                    return item;
                }
            }
        }
        return null;
    }


}
