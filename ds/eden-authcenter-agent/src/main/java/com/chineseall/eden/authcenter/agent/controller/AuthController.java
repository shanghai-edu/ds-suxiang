package com.chineseall.eden.authcenter.agent.controller;

import cn.sh.chineseall.framework.core.util.ArrayUtils;
import cn.sh.chineseall.framework.core.util.StringUtils;
import cn.sh.chineseall.framework.lang.calendar.DateUtils;
import com.alibaba.fastjson.JSON;
import com.chineseall.eden.authcenter.agent.client.*;
import com.chineseall.eden.authcenter.agent.oauth.OauthConfig;
import com.chineseall.eden.authcenter.agent.oauth.OauthConfigItem;
import com.chineseall.eden.authcenter.agent.oauth.OauthType;
import com.chineseall.eden.authcenter.agent.utils.ConvertMessageMapToBean;
import com.chineseall.eden.authcenter.agent.utils.EncodeUtil;
import com.chineseall.eden.authcenter.log.enums.LogType;
import com.chineseall.eden.authcenter.log.model.AuthLog;
import com.chineseall.eden.authcenter.log.model.LogUserInfo;
import com.chineseall.eden.authcenter.log.service.AuthLogService;
import com.sh.chineseall.framework.core.http.resttemplate.MyRestTemplate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class AuthController {

    Logger logger = Logger.getLogger(AuthController.class);

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private OauthConfig oauthConfig;

    @Resource
    private AuthLogService authLogService;



    @RequestMapping(value = {"","auth","auth/index"})
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ds");
        Map<String, ClientItem> clientItemMap = oauthClient.getClients().stream().collect(Collectors.toMap(ClientItem::getClientId, Function.identity()));

        try {
            String clientId = request.getParameter("client_id");

            String returnUrl = request.getParameter("redirect_uri");
            String business_client = request.getParameter("business_client");
            AuthLog authLog = new AuthLog();
            authLog.setLogType(LogType.login);
            if (StringUtils.isNotEmpty(clientId)){

                ClientItem clientItem = clientItemMap.get(clientId);
                if (null == clientItem) {
                    modelAndView.setViewName("error");
                    modelAndView.addObject("message", "client_id为空或者不正确");
                    return modelAndView;
                }
                authLog.setClientId(clientId);
                authLog.setAuthSource(clientItem.getClientName());
                String sign = request.getParameter("sign");
                String signOrigin = clientItem.getClientId() + "$$" + clientItem.getClientSecret();

                String md5Hex = EncodeUtil.md5(signOrigin);
                if (!Objects.equals(sign, md5Hex)) {
                    modelAndView.setViewName("error");
                    modelAndView.addObject("message", "请求不合法");
                    return modelAndView;
                }
                if (StringUtils.isEmpty(returnUrl)) {
                    modelAndView.setViewName("error");
                    modelAndView.addObject("message", "登录成功回调地址为空");
                    return modelAndView;
                }
                String loginType = request.getParameter("login_type");
                if(StringUtils.isNotEmpty(loginType)) {
                    if (StringUtils.isNotEmpty(returnUrl) && (returnUrl.contains("readinglab") || returnUrl.contains("read.etextbook.cn"))){
                        // 阅览室日志分离
                        ClientItem item = clientItemMap.get("readingroomClientId");
                        authLog.setClientId("readingroomClientId");
                        authLog.setAuthSource(item.getClientName());
                    }else if ("cloudcourse".equals(business_client) || "cloudcourse-test".equals(business_client)){
                        ClientItem item = clientItemMap.get("cloudcourse");
                        authLog.setClientId("cloudcourse");
                        authLog.setAuthSource(item.getClientName());

                    }else if ("adaptive-learning".equals(business_client)){
                        ClientItem item = clientItemMap.get("adaptive-learning");
                        authLog.setClientId("adaptive-learning");
                        authLog.setAuthSource(item.getClientName());
                    }

                    OauthType oauthType = OauthType.getValue(loginType);
                    if (oauthType != null){
                        modelAndView.addObject("loginType",loginType);
                    }else {
                        modelAndView.addObject("loginType","");
                    }
                }else {
                    modelAndView.addObject("loginType","");
                }
            }
            authLogService.save(authLog);


            String dianjiaoguanLoinUrl = generateLoginUrl(returnUrl, OauthType.dianjiaoguan.getCode(), authLog.getId());
            modelAndView.addObject("dianjiaoguanLoinUrl", dianjiaoguanLoinUrl);
            String edenoperationLoginUrl = generateLoginUrl(returnUrl, OauthType.edenoperation.getCode(), authLog.getId());
            modelAndView.addObject("edenoperationLoginUrl", edenoperationLoginUrl);

            OauthType[] oauthTypes = OauthType.values();
            for (OauthType oauthType : oauthTypes) {
                if ("idp".equals(oauthType.getType())){

                    String idpAuthTypeUrl = generateSpLoginUrl(returnUrl, oauthType.getCode(), authLog.getId());
                    modelAndView.addObject(oauthType.getCode()+"LoginUrl", idpAuthTypeUrl);
                }
            }

            modelAndView.addObject("logId", authLog.getId());
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "未知错误");
        }
        return modelAndView;
    }

    @RequestMapping("auth/tologin")
    public ModelAndView toLogin(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        String loginUrl = request.getParameter("loginUrl");
        String logId = request.getParameter("logId");
        String oauthType = request.getParameter("oauthType");
        String browser = request.getParameter("browser");
        String version = request.getParameter("version");
        String device = request.getParameter("device");
        String os = request.getParameter("os");
        try {
            AuthLog authLog = authLogService.getById(logId);
            OauthConfigItem oauthConfigItem = oauthConfig.getItems().get(oauthType);
            authLog.setFowardUrl(oauthConfigItem.getOauthUrl());
            authLog.setOauthType(oauthType);
            authLog.setBrowser(browser);
            authLog.setVersion(version);
            authLog.setDevice(device);
            authLog.setOs(os);
            Date now  = new Date();
            String yearMonthDay = DateUtils.dateToString(now, "yyyy-MM-dd");
            String yearMonth = DateUtils.dateToString(now, "yyyy-MM");
            String year = DateUtils.dateToString(now, "yyyy");
            authLog.setYear(year);
            authLog.setYearMonth(yearMonth);
            authLog.setYearMonthDay(yearMonthDay);
            authLogService.save(authLog);
            response.sendRedirect(loginUrl);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "跳转失败,请联系管理人员");
        }
        return modelAndView;
    }

    @RequestMapping("auth/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("logout");
        Map<String, ClientItem> clientItemMap = oauthClient.getClients().stream().collect(Collectors.toMap(ClientItem::getClientId, Function.identity()));
        try {
            String clientId = request.getParameter("client_id");
            ClientItem clientItem = clientItemMap.get(clientId);
            if (clientItem == null) {
                modelAndView.setViewName("error");
                modelAndView.addObject("message", "client_id为空或者不正确");
                return modelAndView;
            }
            String sign = request.getParameter("sign");
            String signOrigin = clientItem.getClientId() + "$$" + clientItem.getClientSecret();

            String md5Hex = EncodeUtil.md5(signOrigin);
            if (!Objects.equals(sign, md5Hex)) {
                modelAndView.setViewName("error");
                modelAndView.addObject("message", "请求不合法");
                return modelAndView;
            }
            String service = request.getParameter("service");
            if (StringUtils.isEmpty(service)) {
                modelAndView.setViewName("error");
                modelAndView.addObject("message", "登出成功回调地址为空");
                return modelAndView;
            }
            String oauthType = request.getParameter("oauth_type");
            if(StringUtils.isEmpty(oauthType)){
                modelAndView.setViewName("error");
                modelAndView.addObject("message", "登出方式为空");
                return modelAndView;
            }
            OauthType type = OauthType.valueOf(oauthType);
            if(type == null){
                modelAndView.setViewName("error");
                modelAndView.addObject("message", "登出方式错误");
                return modelAndView;
            }
            AuthLog authLog = new AuthLog();
            authLog.setOauthType(oauthType);
            authLog.setReturnUrl(service);
            authLog.setClientId(clientId);
            authLog.setAuthSource(clientItem.getClientName());
            authLog.setLogType(LogType.logout);
            authLogService.save(authLog);
            if(StringUtils.isNotEmpty(service)) {
                if (!OauthType.dianjiaoguan.equals(type) && !OauthType.edenoperation.equals(type)){
                    if (OauthType.qpjy.equals(type)){
                        response.sendRedirect(generateSpLogoutUrl(service,type.name()));

                    }

                }else{
                    response.sendRedirect(generateLogoutUrl(service, type.name()));
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "未知错误");
        }
        return modelAndView;
    }

    @RequestMapping(value = "auth/loginsuccess", method = RequestMethod.GET)
    public ModelAndView loginsuccess(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        modelAndView.setViewName("loginsuccess");
        String code = request.getParameter("code");
        String oauthType = request.getParameter("oauth_type");
        String logId = request.getParameter("log_id");
        String returnUrl = request.getParameter("return_url");

        System.out.println("code=" +code);
        System.out.println("oauthType=" +oauthType);
        System.out.println("returnUrl=" +returnUrl);

        AuthLog authLog = authLogService.getById(logId);
        if(authLog == null){
            authLog = new AuthLog();
            authLog.setOauthType(oauthType);
        }
        authLog.setLoginSuccessFlag(true);
        authLog.setReturnUrl(returnUrl);
        String url = generateAccessTokenUrl(code, oauthType);
        try {
            String exchange = MyRestTemplate.exchange(HttpMethod.GET, url, null, null, String.class);
            logger.info("AccessTokenMethod......exchange........."+exchange);
            Map<String, String> keyValueMap = new HashMap<>();
            if (StringUtils.isNotEmpty(exchange)) {
                String[] strings = exchange.split("&");
                for (int i = 0; i < strings.length; i++) {
                    String temp = strings[i];
                    String[] keyValue = temp.split("=");
                    if (ArrayUtils.isNotEmpty(keyValue) && keyValue.length == 2) {
                        keyValueMap.put(keyValue[0], keyValue[1]);
                    }
                }
            }
            String access_token = keyValueMap.get("access_token");
            // 获取用户信息
            if (StringUtils.isNotEmpty(access_token)) {
                String userInfoUrl = generateUserInfoUrl(access_token, oauthType);
                Map<String, Object> userInfoMap = MyRestTemplate.exchange(HttpMethod.GET, userInfoUrl, null, null, HashMap.class);
                // 转换用户信息
                AuthUserInfo userInfo = null;
                if(oauthType.equals(OauthType.dianjiaoguan.toString()))
                    userInfo = ConvertMessageMapToBean.converDianjiaoguanData(userInfoMap);
                else if(oauthType.equals(OauthType.edenoperation.toString()))
                    userInfo = ConvertMessageMapToBean.converEdenoperationData(userInfoMap);
                // 封装下游数据
                ClientDataInfo clientDataInfo = new ClientDataInfo();
                if(userInfo != null) {
                    userInfo.setSource(oauthType);
                    // 封装日志用户信息
                    LogUserInfo logUserInfo = new LogUserInfo();
                    logUserInfo.setRealName(userInfo.getRealName());
                    logUserInfo.setUserId(userInfo.getId());
                    logUserInfo.setStudentCode(userInfo.getStudentCode());
                    logUserInfo.setTeacherTrainingNo(userInfo.getTeacherTrainingNo());
                    if(null != userInfo.getUserType()){
                        logUserInfo.setUserType(userInfo.getUserType().name());
                    }
                    if(StringUtils.isNotEmpty(userInfo.getStudentSchoolId())){
                        logUserInfo.setSchoolId(userInfo.getStudentSchoolId());
                    }
                    if(StringUtils.isNotEmpty(userInfo.getTeacherSchoolId())){
                        logUserInfo.setSchoolId(userInfo.getTeacherSchoolId());
                    }
                    if(StringUtils.isNotEmpty(userInfo.getStudentSchoolName())){
                        logUserInfo.setSchoolName(userInfo.getStudentSchoolName());
                    }
                    if(StringUtils.isNotEmpty(userInfo.getTeacherSchoolName())){
                        logUserInfo.setSchoolName(userInfo.getTeacherSchoolName());
                    }
                    authLog.setGetUserInfoSuccessFlag(true);
                    authLog.setUserInfoMap(userInfoMap);
                    authLog.setLogUserInfo(logUserInfo);
                }
                clientDataInfo.setAuthUserInfo(userInfo);
                clientDataInfo.setOauthType(OauthType.valueOf(oauthType));

                modelAndView.addObject("clientDataInfo", clientDataInfo);
                modelAndView.addObject("successPostUrl", returnUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            authLog.setGetUserInfoSuccessFlag(false);
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "登录失败");
        }
        authLogService.save(authLog);
        return modelAndView;
    }

    @RequestMapping(value = "auth/idp/loginsuccess")
    public ModelAndView idploginsuccess(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
        modelAndView.setViewName("loginsuccess");
        String code = request.getParameter("code");
        String oauthType = request.getParameter("oauth_type");
        String logId = request.getParameter("log_id");
        String returnUrl = request.getParameter("return_url");

        AuthLog authLog = authLogService.getById(logId);
        if(authLog == null){
            authLog = new AuthLog();
            authLog.setOauthType(oauthType);
        }
        authLog.setLoginSuccessFlag(true);
        authLog.setReturnUrl(returnUrl);
        try {
            Map<String, Object> userInfoMap = null;
            // 转换用户信息
            AuthUserInfo userInfo = new AuthUserInfo();
            String userType = request.getParameter("user_type");
            userInfo.setRealName(request.getParameter("real_name"));
            userInfo.setId(request.getParameter("login_name"));
            if ("student".equals(userType)){
                userInfo.setUserType(UserType.STUDENT);
            }else if ("tearcher".equals(userType)){
                userInfo.setUserType(UserType.TEACHER);
            }
            // 封装下游数据
            ClientDataInfo clientDataInfo = new ClientDataInfo();
            if(userInfo != null) {
                userInfo.setSource(oauthType);
                // 封装日志用户信息
                LogUserInfo logUserInfo = new LogUserInfo();
                logUserInfo.setRealName(userInfo.getRealName());
                logUserInfo.setUserId(userInfo.getId());
                logUserInfo.setStudentCode(userInfo.getStudentCode());
                logUserInfo.setTeacherTrainingNo(userInfo.getTeacherTrainingNo());
                if(null != userInfo.getUserType()){
                    logUserInfo.setUserType(userInfo.getUserType().name());
                }
                if(StringUtils.isNotEmpty(userInfo.getStudentSchoolId())){
                    logUserInfo.setSchoolId(userInfo.getStudentSchoolId());
                }
                if(StringUtils.isNotEmpty(userInfo.getTeacherSchoolId())){
                    logUserInfo.setSchoolId(userInfo.getTeacherSchoolId());
                }
                if(StringUtils.isNotEmpty(userInfo.getStudentSchoolName())){
                    logUserInfo.setSchoolName(userInfo.getStudentSchoolName());
                }
                if(StringUtils.isNotEmpty(userInfo.getTeacherSchoolName())){
                    logUserInfo.setSchoolName(userInfo.getTeacherSchoolName());
                }
                authLog.setGetUserInfoSuccessFlag(true);
                authLog.setUserInfoMap(userInfoMap);
                authLog.setLogUserInfo(logUserInfo);
            }
            clientDataInfo.setAuthUserInfo(userInfo);
            clientDataInfo.setOauthType(OauthType.valueOf(oauthType));

            modelAndView.addObject("clientDataInfo", clientDataInfo);
            modelAndView.addObject("successPostUrl", returnUrl);

        } catch (Exception e) {
            e.printStackTrace();
            authLog.setGetUserInfoSuccessFlag(false);
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "登录失败");
        }
        authLogService.save(authLog);
        return modelAndView;
    }


    @RequestMapping(value = "auth/logoutsuccess", method = RequestMethod.GET)
    public ModelAndView loginoutsuccess(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws IOException {
        String returnUrl = request.getParameter("return_url");
        if(StringUtils.isNotEmpty(returnUrl)){
            response.sendRedirect(returnUrl);
        } else {
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "登出失败");
            return modelAndView;
        }
        return null;
    }

    private String generateLoginUrl(String returnUrl, String oauthType, String logId) {
        //oauthConfig + "/authorize?client_id=testClentId&redirect_uri=http%3a%2f%2f192.168.17.129%3a7774%2fauth%2floginsuccess.do&state=state"
        StringBuilder loginUrlBuilder = new StringBuilder();
        OauthConfigItem oauthConfigItem = oauthConfig.getItems().get(oauthType);
        loginUrlBuilder.append(oauthConfigItem.getOauthUrl());
        if (!oauthConfigItem.getOauthUrl().endsWith("/")) {
            loginUrlBuilder.append("/");
        }
        loginUrlBuilder.append("oauth2.0/authorize?client_id=").append(oauthConfigItem.getClientId());
        String loginSuccessUrl = oauthConfigItem.getLoginSuccessUrl();
        loginSuccessUrl = loginSuccessUrl + "?oauth_type=" + oauthType + "&log_id=" + logId;
        if (StringUtils.isNotEmpty(returnUrl)) {
            Map<String, String> param = new HashMap<>();
            param.put("return_url", URLEncoder.encode(returnUrl));
            loginSuccessUrl = appendUrl(loginSuccessUrl, param);
        }
        loginUrlBuilder.append("&redirect_uri=").append(URLEncoder.encode(loginSuccessUrl));

        loginUrlBuilder.append("&state=state&response_type=code");
        return loginUrlBuilder.toString();
    }

    private String generateSpLoginUrl(String returnUrl, String oauthType, String logId) {
        //oauthConfig + "/authorize?client_id=testClentId&redirect_uri=http%3a%2f%2f192.168.17.129%3a7774%2fauth%2floginsuccess.do&state=state"
        StringBuilder loginUrlBuilder = new StringBuilder();
        OauthConfigItem oauthConfigItem = oauthConfig.getItems().get(oauthType);
        loginUrlBuilder.append(oauthConfigItem.getOauthUrl());
        if (!oauthConfigItem.getOauthUrl().endsWith("/")) {
            loginUrlBuilder.append("/");
        }
        loginUrlBuilder.append("login?client_id=").append(oauthConfigItem.getClientId());
        String loginSuccessUrl = oauthConfigItem.getLoginSuccessUrl();
        loginSuccessUrl = loginSuccessUrl + "?log_id=" + logId+"&oauth_type=" + oauthType ;
        if (StringUtils.isNotEmpty(returnUrl)) {
            Map<String, String> param = new HashMap<>();
            param.put("return_url", URLEncoder.encode(returnUrl));
            loginSuccessUrl = appendUrl(loginSuccessUrl, param);
        }
        loginUrlBuilder.append("&redirect_uri=").append(URLEncoder.encode(loginSuccessUrl));
        String sign = EncodeUtil.md5( oauthConfigItem.getClientId()+ "$$" + oauthConfigItem.getClientSecret());
        loginUrlBuilder.append("&sign="+sign);
        return loginUrlBuilder.toString();
    }

    private String generateLogoutUrl(String returnUrl, String oauthType) {
        //oauthConfig + "/authorize?client_id=testClentId&redirect_uri=http%3a%2f%2f192.168.17.129%3a7774%2fauth%2floginsuccess.do&state=state"
        StringBuilder loginUrlBuilder = new StringBuilder();
        OauthConfigItem oauthConfigItem = oauthConfig.getItems().get(oauthType);

        loginUrlBuilder.append(oauthConfigItem.getOauthUrl());
        if (!oauthConfigItem.getOauthUrl().endsWith("/")) {
            loginUrlBuilder.append("/");
        }
        String service = oauthConfigItem.getLogoutSuccessUrl();
        if (StringUtils.isNotEmpty(returnUrl)) {
            Map<String, String> param = new HashMap<>();
            param.put("return_url", URLEncoder.encode(returnUrl));
            service = appendUrl(service, param);
        }
        loginUrlBuilder.append("logout?service=").append(URLEncoder.encode(service));
        return loginUrlBuilder.toString();
    }


    private String generateSpLogoutUrl(String returnUrl, String oauthType) {

        if (oauthType.equals("qpjy")){
            returnUrl = "https://idp.qpedu.cn/logout/logout.html?redirect_url=" + returnUrl;
            returnUrl = "https://sp.etextbook.cn/Shibboleth.sso/Logout?return=" + URLEncoder.encode(returnUrl);
            return returnUrl;
        }
        StringBuilder loginUrlBuilder = new StringBuilder();
        OauthConfigItem oauthConfigItem = oauthConfig.getItems().get(oauthType);

        loginUrlBuilder.append(oauthConfigItem.getOauthUrl());
        if (!oauthConfigItem.getOauthUrl().endsWith("/")) {
            loginUrlBuilder.append("/");
        }
        String service = oauthConfigItem.getLogoutSuccessUrl();
        if (StringUtils.isNotEmpty(returnUrl)) {
            Map<String, String> param = new HashMap<>();
            param.put("return_url", URLEncoder.encode(returnUrl));
            service = appendUrl(service, param);
        }

        String clientId = oauthConfigItem.getClientId();
        String signOrigin = oauthConfigItem.getClientId() + "$$" + oauthConfigItem.getClientSecret();
        String md5Hex = EncodeUtil.md5(signOrigin);

        loginUrlBuilder.append("logout?service=").append(URLEncoder.encode(service));
        loginUrlBuilder.append("&client_id="+clientId);
        loginUrlBuilder.append("&sign="+md5Hex);
        return loginUrlBuilder.toString();
    }


    private String generateUserInfoUrl(String accessToken, String oauthType) {
        //String url = "http://castest.edu.sh.cn/CAS/oauth2.0/accessToken?client_id=testClentId&client_secret=testClientSecret&redirect_uri=http%3a%2f%2f192.168.17.129%3a7774%2fauth%2floginsuccess2.do&code=" + code;
        StringBuilder builder = new StringBuilder();
        OauthConfigItem oauthConfigItem = oauthConfig.getItems().get(oauthType);
        builder.append(oauthConfigItem.getOauthUrl());
        if (!oauthConfigItem.getOauthUrl().endsWith("/")) {
            builder.append("/");
        }
        builder.append("oauth2.0/profile?access_token=").append(accessToken);
        return builder.toString();
    }

    private String generateAccessTokenUrl(String code, String oauthType) {
        //String url = "http://castest.edu.sh.cn/CAS/oauth2.0/accessToken?client_id=testClentId&client_secret=testClientSecret&redirect_uri=http%3a%2f%2f192.168.17.129%3a7774%2fauth%2floginsuccess2.do&code=" + code;
        StringBuilder builder = new StringBuilder();
        OauthConfigItem oauthConfigItem = oauthConfig.getItems().get(oauthType);
        builder.append(oauthConfigItem.getOauthUrl());
        if (!oauthConfigItem.getOauthUrl().endsWith("/")) {
            builder.append("/");
        }
        builder.append("oauth2.0/accessToken?client_id=").append(oauthConfigItem.getClientId());
        builder.append("&client_secret=").append(oauthConfigItem.getClientSecret());
        builder.append("&code=").append(code);
        builder.append("&redirect_uri=").append(oauthConfigItem.getLoginSuccessUrl());
        builder.append("&grant_type=authorization_code");
        return builder.toString();
    }

    /**
     * 在指定url后追加参数
     *
     * @param url
     * @param data 参数集合 key = value
     * @return
     */
    private static String appendUrl(String url, Map<String, String> data) {
        String newUrl = url;
        StringBuffer param = new StringBuffer();
        for (String key : data.keySet()) {
            param.append(key + "=" + data.get(key) + "&");
        }
        String paramStr = param.toString();
        paramStr = paramStr.substring(0, paramStr.length() - 1);
        if (newUrl.indexOf("?") >= 0) {
            newUrl += "&" + paramStr;
        } else {
            newUrl += "?" + paramStr;
        }
        return newUrl;
    }

    @RequestMapping("auth/shauth")
    public ModelAndView chineseallLogin(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("toward");
        Map<String, ClientItem> clientItemMap = oauthClient.getClients().stream().collect(Collectors.toMap(ClientItem::getClientId, Function.identity()));
        try {
            String clientId = request.getParameter("client_id");
            String returnUrl = request.getParameter("redirect_uri");
            ClientItem clientItem = clientItemMap.get(clientId);
            AuthLog authLog = new AuthLog();
            authLog.setLogType(LogType.login);
            if (null == clientItem) {
                modelAndView.setViewName("error");
                modelAndView.addObject("message", "client_id为空或者不正确");
                return modelAndView;
            }
            authLog.setClientId(clientId);
            authLog.setAuthSource(clientItem.getClientName());
            String sign = request.getParameter("sign");
            String signOrigin = clientItem.getClientId() + "$$" + clientItem.getClientSecret();

            String md5Hex = EncodeUtil.md5(signOrigin);
            if (!Objects.equals(sign, md5Hex)) {
                modelAndView.setViewName("error");
                modelAndView.addObject("message", "请求不合法");
                return modelAndView;
            }
            if (StringUtils.isEmpty(returnUrl)) {
                modelAndView.setViewName("error");
                modelAndView.addObject("message", "登录成功回调地址为空");
                return modelAndView;
            }
            authLog.setOauthType(OauthType.dianjiaoguan.getCode());
            OauthConfigItem oauthConfigItem = oauthConfig.getItems().get(OauthType.dianjiaoguan.getCode());
            authLog.setFowardUrl(oauthConfigItem.getOauthUrl());
            authLogService.save(authLog);
            String url = generateLoginUrl(returnUrl, OauthType.dianjiaoguan.getCode(), authLog.getId());
            modelAndView.addObject("loginUrl", url);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "未知错误");
        }
        return modelAndView;
    }

    @RequestMapping("auth/shlogout")
    public ModelAndView shLogout(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("logout");
        Map<String, ClientItem> clientItemMap = oauthClient.getClients().stream().collect(Collectors.toMap(ClientItem::getClientId, Function.identity()));
        try {
            String clientId = request.getParameter("client_id");
            ClientItem clientItem = clientItemMap.get(clientId);
            if (clientItem == null) {
                modelAndView.setViewName("error");
                modelAndView.addObject("message", "client_id为空或者不正确");
                return modelAndView;
            }
            String sign = request.getParameter("sign");
            String signOrigin = clientItem.getClientId() + "$$" + clientItem.getClientSecret();

            String md5Hex = EncodeUtil.md5(signOrigin);
            if (!Objects.equals(sign, md5Hex)) {
                modelAndView.setViewName("error");
                modelAndView.addObject("message", "请求不合法");
                return modelAndView;
            }
            String service = request.getParameter("service");
            if (StringUtils.isEmpty(service)) {
                modelAndView.setViewName("error");
                modelAndView.addObject("message", "登出成功回调地址为空");
                return modelAndView;
            }

            AuthLog authLog = new AuthLog();
            authLog.setOauthType(OauthType.dianjiaoguan.getCode());
            authLog.setReturnUrl(service);
            authLog.setClientId(clientId);
            authLog.setAuthSource(clientItem.getClientName());
            authLog.setLogType(LogType.logout);
            authLogService.save(authLog);
            if(StringUtils.isNotEmpty(service)) {
                response.sendRedirect(generateLogoutUrl(service, OauthType.dianjiaoguan.getCode()));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "未知错误");
        }
        return modelAndView;
    }

}
