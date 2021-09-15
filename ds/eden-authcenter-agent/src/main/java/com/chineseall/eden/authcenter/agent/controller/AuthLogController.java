package com.chineseall.eden.authcenter.agent.controller;

import cn.sh.chineseall.framework.core.repackaged.org.apache.commons.lang3.StringUtils;
import cn.sh.chineseall.framework.core.repackaged.org.apache.commons.lang3.time.DateUtils;
import com.chineseall.eden.authcenter.agent.oauth.OauthType;
import com.chineseall.eden.authcenter.log.service.AuthLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("log")
public class AuthLogController {

    private static final String USER_NAME = "admin";
    private static final String PWD = "chineseall";

    @Resource
    private AuthLogService authLogService;


    @RequestMapping("adminLogin")
    public ModelAndView adminLogin(HttpServletResponse response,HttpServletRequest request) throws IOException{
        response.sendRedirect("loginstat2");
        return null;
    }

    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public ModelAndView doLogin(HttpServletResponse response, HttpServletRequest request, @RequestParam("userName") String userName, @RequestParam("pwd") String pwd) throws IOException {
        response.sendRedirect("loginstat2");
        return null;
    }

    @RequestMapping(value = "loginstat2")
    public ModelAndView loginStat2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        String flag = "loginFlag";//(String) request.getSession().getAttribute("loginFlag");
        if(StringUtils.isNotEmpty(flag)){
            mv.setViewName("statistics");

        } else {
            response.sendRedirect("adminLogin");
            return null;
        }
        return mv;
    }


    @RequestMapping(value = "loginstat")
    public ModelAndView loginStat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        String flag = "loginFlag";//(String) request.getSession().getAttribute("loginFlag");
        if(StringUtils.isNotEmpty(flag)){
            mv.setViewName("statistics");

        } else {
            response.sendRedirect("adminLogin");
            return null;
        }
        return mv;
    }

    @RequestMapping(value = "loginstat_xhzy")
    public ModelAndView loginStatXhzy(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        String flag = (String) request.getSession().getAttribute("loginFlag");
        if(StringUtils.isNotEmpty(flag)){
            mv.setViewName("loginstat_xhzy");
            // 累计登录人次
            Map<String, Object> params1 = new HashMap<>();
            params1.put("authSource", "第三方接入");
            params1.put("getUserInfoSuccessFlag", true);
            Long loginCounts = authLogService.countLoginByParams(params1);
            mv.addObject("data1_total_djg", loginCounts);
            // 近30日累计登录人次
            Map<String, Object> params4 = new HashMap<>();
            params4.put("authSource", "第三方接入");
            params4.put("getUserInfoSuccessFlag", true);
            params4.put("beginTime", getZeroTime(DateUtils.addDays(new Date(), -29)));
            Long loginCounts30Day = authLogService.countLoginByParams(params4);
            mv.addObject("data1_30_djg", loginCounts30Day);
            // 近7日累计登录人次
            Map<String, Object> params7 = new HashMap<>();
            params7.put("authSource", "第三方接入");
            params7.put("getUserInfoSuccessFlag", true);
            params7.put("beginTime", getZeroTime(DateUtils.addDays(new Date(), -6)));
            Long loginCounts7Day = authLogService.countLoginByParams(params7);
            mv.addObject("data1_7_djg", loginCounts7Day);
            // 近7日登录人次查询
            List<String> dates = new ArrayList<>();
            List<Long> dianjiaoguanDay7counts = new ArrayList<>();
            for(int i = 0;i<7;i++){
                dates.add(getDateDtr(DateUtils.addDays(new Date(), -(6-i))));
                Map<String, Object> params = new HashMap<>();
                params.put("authSource", "第三方接入");
                params.put("getUserInfoSuccessFlag", true);
                params.put("beginTime", getZeroTime(DateUtils.addDays(new Date(), -(6-i))));
                params.put("endTime", getEndTime(DateUtils.addDays(new Date(), -(6-i))));
                Long dianjiaoguanCounts = authLogService.countLoginByParams(params);
                dianjiaoguanDay7counts.add(dianjiaoguanCounts);
            }
            mv.addObject("data1_time", dates);
            mv.addObject("data1_djg", dianjiaoguanDay7counts);
        } else {
            response.sendRedirect("adminLogin");
            return null;
        }
        return mv;
    }


    private  Map<String, String> getLatest7DatesMap(Date date){
        Map<String, String> map = new LinkedHashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        map.put(format.format(date), cal.get(Calendar.MONTH)+1 + "." + (cal.get(Calendar.DAY_OF_MONTH)) );
        for(int i = 0; i<6 ;i++) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
            Date newDate = cal.getTime();
            map.put(format.format(newDate), cal.get(Calendar.MONTH)+1 + "." + (cal.get(Calendar.DAY_OF_MONTH)) );
        }
        return map;
    }


    private String getDateDtr(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH)+1 + "." + (cal.get(Calendar.DAY_OF_MONTH));
    }

    private Date getZeroTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getEndTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
}
