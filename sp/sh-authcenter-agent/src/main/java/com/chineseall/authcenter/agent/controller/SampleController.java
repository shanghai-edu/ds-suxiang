package com.chineseall.authcenter.agent.controller;

import cn.sh.chineseall.framework.core.util.StringUtils;
import com.alibaba.fastjson.JSON;

import com.chineseall.authcenter.agent.client.ClientDataInfo;
import com.chineseall.authcenter.agent.utils.EncodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;


@Controller
@RequestMapping("sample")
public class SampleController {

    // 认证系统提供client_id
    private static final String CLIENT_ID = "jsydClentId";
    // 认证系统提供client_secret
    private static final String CLIENT_SECRET = "jsydClientSecret";
    // 登录地址
    private static final String LOGIN_URL = "https://sp.etextbook.cn/authcenter/auth/login";
    // 登出地址
    private static final String LOGOUT_URL = "https://sp.etextbook.cn/authcenter/auth/logout";
    // 登录回调地址
    private static final String LOGIN_SUCCESS_URL = "https://sp.etextbook.cn/authcenter/sample/loginsuccess";
    // 登出回调地址
    private static final String LOGOUT_SUCCESS_URL = "https://sp.etextbook.cn/authcenter/sample/logoutsuccess";


    @RequestMapping("login")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            String loginType = request.getParameter("login_type");
            String sign = EncodeUtil.md5(CLIENT_ID + "$$" + CLIENT_SECRET);
            String loginUrl = LOGIN_URL + "?client_id=" + CLIENT_ID + "&sign=" + sign + "&redirect_uri=" + URLEncoder.encode(LOGIN_SUCCESS_URL);
            if(loginType!=null){
                loginUrl = loginUrl + "&login_type=" + loginType;
            }
            response.sendRedirect(loginUrl);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping("logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            String sign = EncodeUtil.md5(CLIENT_ID + "$$" + CLIENT_SECRET);
            String loginUrl = LOGOUT_URL + "?client_id=" + CLIENT_ID + "&sign=" + sign + "&service="
                    + URLEncoder.encode(LOGOUT_SUCCESS_URL) ;
            response.sendRedirect(loginUrl);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }


    @RequestMapping("loginsuccess")
    @ResponseBody
    public String loginsuccess(ClientDataInfo clientDataInfo) {
        return JSON.toJSONString(clientDataInfo);
    }


    @RequestMapping("logoutsuccess")
    @ResponseBody
    public String logoutsuccess(ClientDataInfo clientDataInfo) {
        return "登出成功！";
    }
}
