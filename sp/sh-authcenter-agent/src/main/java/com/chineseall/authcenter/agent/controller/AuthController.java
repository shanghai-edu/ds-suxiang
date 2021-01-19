package com.chineseall.authcenter.agent.controller;

import cn.sh.chineseall.framework.core.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.chineseall.authcenter.agent.client.AuthUserInfo;
import com.chineseall.authcenter.agent.client.ClientDataInfo;
import com.chineseall.authcenter.agent.client.ClientItem;
import com.chineseall.authcenter.agent.client.OauthClient;
import com.chineseall.authcenter.agent.oauth.OauthType;
import com.chineseall.authcenter.agent.utils.CookiesUtil;
import com.chineseall.authcenter.agent.utils.EncodeUtil;
import com.chineseall.authcenter.log.enums.LogType;
import com.chineseall.authcenter.log.model.AuthLog;
import com.chineseall.authcenter.log.service.AuthLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private OauthClient oauthClient;

    @Resource
    private AuthLogService authLogService;

    private static final String COOKIE_LOGIN_TYPE = "col_login_type";

    private static final String COOKIE_LOG_ID = "col_log_id";

    private static final String JSYD_CLIENT_ID = "jsydClentId";

    private static final String LOGOUT_URL = "https://sp.etextbook.cn/Shibboleth.sso/Logout";

    /**
     * 登陆入口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("login")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        Map<String, ClientItem> clientItemMap = oauthClient.getClients().stream().collect(Collectors.toMap(ClientItem::getClientId, Function.identity()));
        try {
            String clientId = request.getParameter("client_id");
            String returnUrl = request.getParameter("redirect_uri");
            System.out.println("redirect_uri1111111111111====="+returnUrl);
            String authSource = request.getParameter("auth_source");
            ClientItem clientItem = clientItemMap.get(clientId);
            if (null == clientItem) {
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
            if (StringUtils.isEmpty(returnUrl)) {
                modelAndView.setViewName("error");
                modelAndView.addObject("message", "登录成功回调地址为空");
                return modelAndView;
            }
            if(StringUtils.isNotEmpty(authSource)){
                OauthType oauthType = OauthType.valueOf(authSource);
                if(oauthType==null) {
                    modelAndView.setViewName("error");
                    modelAndView.addObject("message", "参数错误");
                    return modelAndView;
                }
                CookiesUtil.setCookie(response, null, COOKIE_LOGIN_TYPE, oauthType.getCode(), 1);
            } if(clientItem.getLoginWay() !=null && clientItem.getLoginWay() == 1) {
                CookiesUtil.setCookie(response, null, COOKIE_LOGIN_TYPE, clientItem.getLoginGoal().getCode(), 1);
            }

            AuthLog authLog = new AuthLog();
            authLog.setLogType(LogType.login);
            authLog.setReturnUrl(returnUrl);
            authLog.setClientId(clientId);
            authLog.setClientName(clientItemMap.get(clientId).getClientName());
            authLogService.save(authLog);
            CookiesUtil.setCookie(response, null, COOKIE_LOG_ID, authLog.getId(), 1);
            CookiesUtil.setCookie(response, null, "return_url", returnUrl, 1);
            Map<String, String> params = new HashMap<>();
            params.put("redirect_uri", returnUrl);
            params.put("auth_log_id", authLog.getId());
            params.put("client_id", clientId);
            response.sendRedirect(appendUrl("/auth/index", params));
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "未知错误");
        }
        return modelAndView;
    }

    /**
     * idp选择页面
     * @param request
     * @return
     */
    @RequestMapping("/idp")
    public ModelAndView auth(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mv = new ModelAndView("idp");
        String loginType = CookiesUtil.getCookie(request, COOKIE_LOGIN_TYPE);
        if(null != loginType) {
            mv.addObject("loginType", loginType);
            CookiesUtil.delCookie(response, null, "/", COOKIE_LOGIN_TYPE, loginType);
        }
        return mv;
    }

    @RequestMapping("tologin")
    public ModelAndView toLogin(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        String loginUrl = request.getParameter("loginUrl");
        System.out.println("loginUrl222222222222222====="+loginUrl);
        String idp = request.getParameter("idp");
        try {
            String authLogId = CookiesUtil.getCookie(request, COOKIE_LOG_ID);
            if(null != authLogId) {
                AuthLog authLog = authLogService.getById(authLogId);
                authLog.setIdp(idp);
                authLogService.save(authLog);
                CookiesUtil.delCookie(response, null, "/", COOKIE_LOG_ID, authLogId);
            }
            response.sendRedirect(loginUrl);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "跳转失败,请联系管理人员");
        }
        return modelAndView;
    }

    /**
     * 登陆受保护地址+登陆成功回跳地址
     * @param request
     * @param response
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/index")
    public ModelAndView loginsuccess(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws IOException {
        modelAndView.setViewName("loginsuccess");
        String returnUrl = request.getParameter("redirect_uri");
        String cookieReturnUrl = CookiesUtil.getCookie(request, "return_url");
        if(cookieReturnUrl!=null){
            returnUrl = cookieReturnUrl;
        }
        System.out.println("redirect_uri33333333333333333====="+returnUrl);
        String authLogId = request.getParameter("auth_log_id");
        ClientDataInfo clientDataInfo = getUserInfoData(request);
        AuthLog authLog = authLogService.getById(authLogId);
        authLog.setLoginSuccessFlag(true);
        if(clientDataInfo!=null && clientDataInfo.getAuthUserInfo()!=null){
            authLog.setUserInfoMap(JSON.parseObject(JSON.toJSONString(clientDataInfo.getAuthUserInfo()), Map.class));
        }
        String shibProvider = (String) request.getAttribute("Shib-Identity-Provider");
        authLog.setShibProvider(shibProvider);
        authLogService.save(authLog);
        String clientId = request.getParameter("client_id");

        Map<String, String> params = new HashMap<>();
        params.put("login_name", clientDataInfo.getAuthUserInfo().getLoginName());
        params.put("real_name", clientDataInfo.getAuthUserInfo().getRealName());
        params.put("user_type", clientDataInfo.getAuthUserInfo().getUserType());
        response.sendRedirect(appendUrl(returnUrl, params));
        return null;
    }

    private ClientDataInfo getUserInfoData(HttpServletRequest request){
        ClientDataInfo clientDataInfo = new ClientDataInfo();
        clientDataInfo.setReturnUrl(request.getParameter("redirect_uri"));
        AuthUserInfo authUserInfo = new AuthUserInfo();
        authUserInfo.setLoginName(toUtf8(request.getAttribute("uid")));
        authUserInfo.setRealName(toUtf8(request.getAttribute("cn")));
        authUserInfo.setUserType(toUtf8(request.getAttribute("typeOf")));
        authUserInfo.setId(toUtf8(request.getAttribute("uid")));
        clientDataInfo.setAuthUserInfo(authUserInfo);

        Map<String, Object> map = new HashMap<String, Object>();
        String atrStr = (String) request.getAttribute("Shib-Identity-Provider");
        map.put("Shib-Identity-Provider", atrStr);
        map.put("uid", toUtf8(request.getAttribute("uid")));
        map.put("cn", toUtf8(request.getAttribute("cn")));
        map.put("domainName", toUtf8(request.getAttribute("domainName")));
        map.put("typeOf", toUtf8(request.getAttribute("typeOf")));
        map.put("eduID", toUtf8(request.getAttribute("eduID")));
        map.put("shEduPersonUserId", toUtf8(request.getAttribute("shEduPersonUserId")));
        map.put("shEduPersonDateOfBirth", toUtf8(request.getAttribute("shEduPersonDateOfBirth")));
        map.put("shEduPersonGender", toUtf8(request.getAttribute("shEduPersonGender")));
        map.put("shEduPersonHomeOrganization", toUtf8(request.getAttribute("shEduPersonHomeOrganization")));
        map.put("shEduPersonHomeOrganizationType", toUtf8(request.getAttribute("shEduPersonHomeOrganizationType")));
        map.put("shEduPersonDepartment", toUtf8(request.getAttribute("shEduPersonDepartment")));
        map.put("shEduPersonMajor", toUtf8(request.getAttribute("shEduPersonMajor")));
        map.put("shEduPersonMatriculationDate", toUtf8(request.getAttribute("shEduPersonMatriculationDate")));
        map.put("shEduPersionStageOfStudy", toUtf8(request.getAttribute("shEduPersionStageOfStudy")));
        map.put("shEduPersonGrade", toUtf8(request.getAttribute("shEduPersonGrade")));
        map.put("shEduPersonClass", toUtf8(request.getAttribute("shEduPersonClass")));
        map.put("shEduPersonSchool", toUtf8(request.getAttribute("shEduPersonSchool")));
        map.put("shEduId", toUtf8(request.getAttribute("shEduId")));
        System.out.println(JSON.toJSONString(map));
        return clientDataInfo;
    }
    private String toUtf8(Object ob){
        if(ob==null){
            return null;
        }else{
            try{
                String s = (String) ob;
                if(s.equals(new String(s.getBytes("iso8859-1"),"iso8859-1"))){
                    return new String(s.getBytes("iso8859-1"),"utf-8");
                }else{
                    return s;
                }
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
    }

    @RequestMapping("logout")
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
            AuthLog authLog = new AuthLog();
            authLog.setLogType(LogType.logout);
            authLog.setReturnUrl(service);
            authLog.setClientId(clientId);
            authLog.setClientName(clientItem.getClientName());
            authLogService.save(authLog);
            //response.sendRedirect("/Shibboleth.sso/Logout?return="+URLEncoder.encode(service));
            modelAndView.addObject("logoutUrl", LOGOUT_URL);
            modelAndView.addObject("returnUrl", service);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.setViewName("error");
            modelAndView.addObject("message", "未知错误");
        }
        return modelAndView;
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


}
