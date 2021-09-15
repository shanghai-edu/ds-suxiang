package com.chineseall.authcenter.agent;

import cn.sh.chineseall.framework.starter.annotation.AlphaFrameworkApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@AlphaFrameworkApplication
@ComponentScan(basePackages = {"com.chineseall.authcenter.agent", "com.chineseall.authcenter.log"})
public class EdenAuthcenterAgentApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EdenAuthcenterAgentApplication.class);
    }
    public static void main(String[] args) {
        cn.sh.chineseall.framework.springboot.bootstrap.application.AlphaFrameworkSpringApplication.run(EdenAuthcenterAgentApplication.class, args);
    }
}
