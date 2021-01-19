package com.chineseall.eden.authcenter.agent.controller;

import cn.sh.chineseall.framework.api.MapMessage;
import com.chineseall.eden.authcenter.agent.vo.StatItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("logstatistics")
public class LogStatisticsController {

    /**
     * APP使用情况统计
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("app")
    @ResponseBody
    public MapMessage applications(@RequestParam(value = "clientId",required = false) String clientId, @RequestParam(value = "year",required = false) Integer year, @RequestParam(value = "month",required = false) Integer month){
        List<StatItem> result = new ArrayList<>();
        StatItem item1 = new StatItem();
        item1.setName("应用A");
        item1.setCount(1000);
        StatItem item2 = new StatItem();
        item2.setName("应用B");
        item2.setCount(1000);
        result.add(item1);
        result.add(item2);
        return MapMessage.successMessage().add("data",result);
    }

    /**
     * 来源环境情况统计
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("environment")
    @ResponseBody
    public MapMessage environments(@RequestParam(value = "clientId",required = false) String clientId, @RequestParam(value = "year",required = false) Integer year, @RequestParam(value = "month",required = false) Integer month){
        Map<String,List<StatItem>> result = new HashMap<>();


        List<StatItem> browserResult = new ArrayList<>();
        StatItem item1 = new StatItem();
        item1.setName("浏览器1");
        item1.setCount(1000);
        StatItem item2 = new StatItem();
        item2.setName("浏览器2");
        item2.setCount(1000);
        browserResult.add(item1);
        browserResult.add(item2);
        result.put("browser",browserResult);

        List<StatItem> osResult = new ArrayList<>();
        StatItem os1 = new StatItem();
        os1.setName("操作系统1");
        os1.setCount(1000);
        StatItem os2 = new StatItem();
        os2.setName("操作系统2");
        os2.setCount(1000);
        osResult.add(os1);
        osResult.add(os2);
        result.put("os",osResult);

        return MapMessage.successMessage().add("data",result);
    }


    /**
     * 认证数据统计
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("auth")
    @ResponseBody
    public MapMessage auths(@RequestParam(value = "clientId",required = false) String clientId, @RequestParam(value = "year",required = false) Integer year, @RequestParam(value = "month",required = false) Integer month){
        Map<String,Object> result = new HashMap<>();

        result.put("all_auth",1500000);
        result.put("cas_edu_auth",100000);

        List<StatItem> browserResult = new ArrayList<>();
        StatItem item1 = new StatItem();
        item1.setName("黄浦区");
        item1.setCount(1000);
        StatItem item2 = new StatItem();
        item2.setName("青浦区");
        item2.setCount(1000);
        browserResult.add(item1);
        browserResult.add(item2);
        result.put("area_auth",browserResult);
        return MapMessage.successMessage().add("data",result);
    }

    /**
     * 认证高峰时间数据统计
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("auth_pick_times")
    @ResponseBody
    public MapMessage authPickTimes(@RequestParam(value = "clientId",required = false) String clientId, @RequestParam(value = "year",required = false) Integer year, @RequestParam(value = "month",required = false) Integer month){

        List<StatItem> result = new ArrayList<>();
        StatItem item1 = new StatItem();
        item1.setName("2020-01");
        item1.setCount(1000);
        StatItem item2 = new StatItem();
        item2.setName("2020-02");
        item2.setCount(1000);
        result.add(item1);
        result.add(item2);
        return MapMessage.successMessage().add("data",result);
    }


    /**
     * 认证身份统计
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("auth_user_type")
    @ResponseBody
    public MapMessage authUserTypes(@RequestParam(value = "clientId",required = false) String clientId, @RequestParam(value = "year",required = false) Integer year, @RequestParam(value = "month",required = false) Integer month){

        List<StatItem> result = new ArrayList<>();
        StatItem item1 = new StatItem();
        item1.setName("老师");
        item1.setCount(1000);
        StatItem item2 = new StatItem();
        item2.setName("学生");
        item2.setCount(1000);
        result.add(item1);
        result.add(item2);
        return MapMessage.successMessage().add("data",result);
    }


    /**
     * 认证热点单位统计
     * @param year
     * @param month
     * @return
     */
    @RequestMapping("auth_hot_org")
    @ResponseBody
    public MapMessage authHotOrgs(@RequestParam(value = "clientId",required = false) String clientId, @RequestParam(value = "year",required = false) Integer year, @RequestParam(value = "month",required = false) Integer month){

        List<StatItem> result = new ArrayList<>();
        StatItem item1 = new StatItem();
        item1.setName("A学校");
        item1.setCount(1000);
        StatItem item2 = new StatItem();
        item2.setName("B学校");
        item2.setCount(1000);
        result.add(item1);
        result.add(item2);
        return MapMessage.successMessage().add("data",result);
    }



    /**
     * 认证接入的APP
     * @return
     */
    @RequestMapping("auth_apps")
    @ResponseBody
    public MapMessage authApps(){

        List<StatItem> result = new ArrayList<>();
        StatItem item1 = new StatItem();
        item1.setName("数字教材");
        item1.setCode("asfsafdsf");
        StatItem item2 = new StatItem();
        item2.setName("阅览室");
        item2.setCode("safsadfasd");
        result.add(item1);
        result.add(item2);
        return MapMessage.successMessage().add("data",result);
    }







}