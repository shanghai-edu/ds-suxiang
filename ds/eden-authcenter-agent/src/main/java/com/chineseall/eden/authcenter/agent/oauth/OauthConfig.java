package com.chineseall.eden.authcenter.agent.oauth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "oauth")
public class OauthConfig implements Serializable {

    Map<String,OauthConfigItem> items;

}
