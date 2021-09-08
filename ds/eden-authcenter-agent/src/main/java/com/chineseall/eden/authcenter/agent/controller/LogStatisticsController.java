package com.chineseall.eden.authcenter.agent.controller;

import cn.sh.chineseall.framework.api.MapMessage;
import cn.sh.chineseall.framework.core.util.CollectionUtils;
import cn.sh.chineseall.framework.lang.calendar.DateUtils;
import com.chineseall.eden.authcenter.agent.client.ClientItem;
import com.chineseall.eden.authcenter.agent.client.OauthClient;
import com.chineseall.eden.authcenter.agent.oauth.OauthConfig;
import com.chineseall.eden.authcenter.agent.vo.StatItem;
import com.chineseall.eden.authcenter.log.model.AuthLog;
import com.chineseall.eden.authcenter.log.service.AuthLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("logstatistics")
public class LogStatisticsController {

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private OauthConfig oauthConfig;

    @Resource
    private AuthLogService authLogService;

    private final static String[] areas = new String[]{
            "黄浦区",
            "徐汇区",
            "长宁区",
            "静安区",
            "普陀区",
            "虹口区",
            "杨浦区",
            "闵行区",
            "宝山区",
            "嘉定区",
            "浦东区",
            "金山区",
            "松江区",
            "青浦区",
            "奉贤区",
            "崇明区"};

    /**
     * APP使用情况统计
     *
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("app")
    @ResponseBody
    public MapMessage applications(@RequestParam(value = "clientId", required = false) String clientId, @RequestParam(value = "year", required = false) String year, @RequestParam(value = "month", required = false) String month) {
        List<StatItem> result = new ArrayList<>();
        List<ClientItem> statClients = new ArrayList<>();
        if (clientId != null){
            ClientItem clientItem = oauthClient.getClientItem(clientId);
            if (null != clientItem){
                statClients.add(clientItem);
            }
        }else {
            statClients.addAll(oauthClient.getClients());
        };
        Map<String,Object> param = new HashMap<>();
        param.put("loginSuccessFlag", true);
        if (null != year){
            param.put("year", year);
        }
        if (null != month){
            param.put("yearMonth", month);
        }
        statClients.forEach(item -> {
            StatItem statItem = new StatItem();
            statItem.setCode(item.getClientId());
            statItem.setName(item.getClientName());
            param.put("clientId",item.getClientId());
            long count = authLogService.count(param);
            statItem.setCount(count);
            result.add(statItem);
        });
        return MapMessage.successMessage().add("data", result);
    }

    /**
     * 来源环境情况统计
     *
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("environment")
    @ResponseBody
    public MapMessage environments(@RequestParam(value = "clientId", required = false) String clientId, @RequestParam(value = "year", required = false) String year, @RequestParam(value = "month", required = false) String month) {
        Map<String, List<StatItem>> result = new HashMap<>();
        Map<String,Object> param = new HashMap<>();
        param.put("loginSuccessFlag", true);
        if (clientId != null){
            param.put("clientId",clientId);
        }
        if (null != year){
            param.put("year", year);
        }
        if (null != month){
            param.put("yearMonth", month);
        }
        long all_auth = authLogService.count(param);
        List<StatItem> browserResult = new ArrayList<>();
        List<String> browserList = authLogService.distinct("browser", param);
        long unknownBrowser = all_auth;
        if (CollectionUtils.isNotEmpty(browserList)){
            for (String item : browserList) {
                param.put("browser",item);
                long count = authLogService.count(param);
                StatItem statItem = new StatItem();
                statItem.setName(item);
                statItem.setCount(count);
                unknownBrowser = unknownBrowser - count;
                browserResult.add(statItem);
            }
        }
        if (unknownBrowser > 0){
            StatItem statItem = new StatItem();
            statItem.setName("unknown");
            statItem.setCount(unknownBrowser);
            browserResult.add(statItem);
        }


        result.put("browser", browserResult);



        List<StatItem> osResult = new ArrayList<>();

        param.remove("browser");
        List<String> osList = authLogService.distinct("os", param);

        long unknownOs = all_auth;
        if (CollectionUtils.isNotEmpty(osList)){
            for (String item : osList) {
                param.put("os",item);
                long count = authLogService.count(param);
                StatItem statItem = new StatItem();
                statItem.setName(item);
                statItem.setCount(count);
                osResult.add(statItem);
                unknownOs = unknownOs - count;
            }
        }

        if (unknownOs > 0){
            StatItem statItem = new StatItem();
            statItem.setName("unknown");
            statItem.setCount(unknownOs);
            osResult.add(statItem);
        }
        result.put("os", osResult);
        return MapMessage.successMessage().add("data", result);
    }


    /**
     * 认证数据统计
     *
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("auth")
    @ResponseBody
    public MapMessage auths(@RequestParam(value = "clientId", required = false) String clientId, @RequestParam(value = "year", required = false) String year, @RequestParam(value = "month", required = false) String month) {
        Map<String, Object> result = new HashMap<>();

        Map<String,Object> param = new HashMap<>();
        param.put("loginSuccessFlag", true);
        if (clientId != null){
            param.put("clientId",clientId);
        }
        if (null != year){
            param.put("year", year);
        }
        if (null != month){
            param.put("yearMonth", month);
        }

        long all_auth = authLogService.count(param);

        result.put("all_auth", all_auth);

        param.put("oauthType","dianjiaoguan");
        long cas_edu_auth = authLogService.count(param);
        result.put("cas_edu_auth", cas_edu_auth);

        param.put("oauthType","qpjy");
        long qp_auth = authLogService.count(param);
        result.put("qp_auth", qp_auth);

        param.put("oauthType","edenoperation");
        long operator_auth = authLogService.count(param);
        result.put("operator_auth", operator_auth);

        param.remove("oauthType");
        List<String> loginUsers = authLogService.distinct("logUserInfo.userId", param);
        result.put("num_of_people", loginUsers.size());

//qp_auth，operator_auth,num_of_people
//        List<StatItem> browserResult = new ArrayList<>();
//        for (String area : areas) {
//            StatItem item = new StatItem();
//            item.setName(area);
//            if (area.equals("青浦区")){
//                param.put("oauthType","qpjy");
//                long qpjy = authLogService.count(param);
//                item.setCount(qpjy);
//            }
//            browserResult.add(item);
//        }
//        browserResult.sort(Comparator.comparing(StatItem::getCount).reversed());
//        result.put("area_auth", browserResult);
        return MapMessage.successMessage().add("data", result);
    }

    /**
     * 认证高峰时间数据统计
     *
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("auth_pick_times")
    @ResponseBody
    public MapMessage authPickTimes(@RequestParam(value = "clientId", required = false) String clientId, @RequestParam(value = "year", required = false) String year, @RequestParam(value = "month", required = false) String month) {


        Map<String,Object> param = new HashMap<>();
        param.put("loginSuccessFlag", true);
        if (clientId != null){
            param.put("clientId",clientId);
        }
        if (null != year){
            param.put("year", year);
        }
        if (null != month){
            param.put("yearMonth", month);
        }


        List<StatItem> result = new ArrayList<>();
        if (month !=null){
            List<String> list = authLogService.distinct("yearMonthDay", param);
            if (CollectionUtils.isNotEmpty(list)){
                for (String ym : list) {
                    param.put("yearMonthDay",ym);
                    long count = authLogService.count(param);
                    StatItem item = new StatItem();
                    item.setName(ym);
                    item.setCount(count);
                    result.add(item);
                }
            }
        }else {
            List<String> list = authLogService.distinct("yearMonth", param);
            if (CollectionUtils.isNotEmpty(list)){
                for (String ym : list) {
                    param.put("yearMonth",ym);
                    long count = authLogService.count(param);
                    StatItem item = new StatItem();
                    item.setName(ym);
                    item.setCount(count);
                    result.add(item);
                }
            }
        }
        result.sort(Comparator.comparing(StatItem::getName));
        return MapMessage.successMessage().add("data", result);
    }


    /**
     * 认证身份统计
     *
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("auth_user_type")
    @ResponseBody
    public MapMessage authUserTypes(@RequestParam(value = "clientId", required = false) String clientId, @RequestParam(value = "year", required = false) String year, @RequestParam(value = "month", required = false) String month) {

        Map<String,Object> param = new HashMap<>();
        param.put("loginSuccessFlag", true);
        if (clientId != null){
            param.put("clientId",clientId);
        }
        if (null != year){
            param.put("year", year);
        }
        if (null != month){
            param.put("yearMonth", month);
        }


        long all_auth = authLogService.count(param);


        param.put("logUserInfo.userType","TEACHER");
        long teacherCount = authLogService.count(param);
        param.put("logUserInfo.userType","STUDENT");
        long studentCount = authLogService.count(param);


        List<StatItem> result = new ArrayList<>();
        StatItem item1 = new StatItem();
        item1.setName("老师");
        item1.setCount(teacherCount);
        StatItem item2 = new StatItem();
        item2.setName("学生");
        item2.setCount(studentCount);

        StatItem other = new StatItem();
        other.setName("其他");
        other.setCount(all_auth - teacherCount - studentCount);

        result.add(item1);
        result.add(item2);
        result.add(other);
        return MapMessage.successMessage().add("data", result);
    }


    /**
     * 认证热点单位统计
     *
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("auth_hot_org")
    @ResponseBody
    public MapMessage authHotOrgs(@RequestParam(value = "clientId", required = false) String clientId, @RequestParam(value = "year", required = false) String year, @RequestParam(value = "month", required = false) String month) {
        Map<String,Object> param = new HashMap<>();
        param.put("loginSuccessFlag", true);
        if (clientId != null){
            param.put("clientId",clientId);
        }
        if (null != year){
            param.put("year", year);
        }
        if (null != month){
            param.put("yearMonth", month);
        }

        List<String> schoolNames = authLogService.distinct("logUserInfo.schoolName", param);
        List<StatItem> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(schoolNames)){
            for (String name : schoolNames) {

                param.put("logUserInfo.schoolName",name);
                long count = authLogService.count(param);

                StatItem item = new StatItem();
                item.setCount(count);
                item.setName(name);
                result.add(item);
            }
        }
        result.sort(Comparator.comparing(StatItem::getCount).reversed());

        if (result.size()>10){
            result=result.subList(0,10);
        }
        return MapMessage.successMessage().add("data", result);
    }


    /**
     * 认证接入的APP
     *
     * @return
     */
    @RequestMapping("auth_apps")
    @ResponseBody
    public MapMessage authApps() {

        List<StatItem> result = new ArrayList<>();
        List<ClientItem> clients = oauthClient.getClients();
        if (CollectionUtils.isNotEmpty(clients)){
            clients.forEach(item -> {
                StatItem statItem = new StatItem();
                statItem.setCode(item.getClientId());
                statItem.setName(item.getClientName());
                result.add(statItem);
            });
        }
        return MapMessage.successMessage().add("data", result);
    }

    /**
     * 认证接入的APP
     *
     * @return
     */
    @RequestMapping("sourcedata")
    @ResponseBody
    public MapMessage sourcedata() {
        int i = 1;
        for (;;){
            Map<String,Object> param = new HashMap<>();
            param.put("loginSuccessFlag", true);
            List<AuthLog> authLogs = authLogService.listLogWithPage(i, 1000,param);
            if (CollectionUtils.isEmpty(authLogs)){
                break;
            }
            authLogs.forEach(item -> {
                Date createTime = item.getCreateTime();
                if (null == item.getYearMonthDay()){
                    item.setYearMonthDay(DateUtils.format(createTime,"yyyy-MM-dd"));
                }
                if (null == item.getYearMonth()) {
                    item.setYearMonth(DateUtils.format(createTime, "yyyy-MM"));
                }
                if (null == item.getYear()) {
                    item.setYear(DateUtils.format(createTime, "yyyy"));
                }
                if (null == item.getClientId()){
                    if (item.getReturnUrl()!= null && item.getReturnUrl().contains("readinglab")){
                        item.setClientId("readingroomClientId");
                    }else {
                        item.setClientId("testClentId");
                    }
                }else {
                    if (item.getReturnUrl()!= null && item.getReturnUrl().contains("readinglab")){
                        item.setClientId("readingroomClientId");
                    }
                }
                if (item.getOauthType() == null){
                    if (item.getUserInfoMap() != null) {
                        item.setOauthType("dianjiaoguan");
                    }else {

                        item.setOauthType("edenoperation");
                    }
                }
                authLogService.replace(item);
            });
            i++;

        }
        return MapMessage.successMessage();
    }
}