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
@RequestMapping("com/chineseall/eden/authcenter/log")
public class AuthLogController {

    private static final String USER_NAME = "admin";
    private static final String PWD = "chineseall";

    @Resource
    private AuthLogService authLogService;


    @RequestMapping("adminLogin")
    public ModelAndView adminLogin(HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("adminlogin");
        return mv;
    }

    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public ModelAndView doLogin(HttpServletResponse response, HttpServletRequest request, @RequestParam("userName") String userName, @RequestParam("pwd") String pwd) throws IOException {
        ModelAndView mv = new ModelAndView();
        if(userName.equals(USER_NAME) && pwd.equals(PWD)){
            request.getSession().setAttribute("loginFlag", "1");
            response.sendRedirect("loginstat");
        } else {
            mv.setViewName("error");
            mv.addObject("message", "账号或密码错误");
            return mv;
        }
        return null;
    }


    @RequestMapping(value = "loginstat")
    public ModelAndView loginStat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        String flag = (String) request.getSession().getAttribute("loginFlag");
        if(StringUtils.isNotEmpty(flag)){
            mv.setViewName("loginstat");
            // 累计登录人次
            Map<String, Object> params1 = new HashMap<>();
            params1.put("authSource", "上海运营中心");
            params1.put("getUserInfoSuccessFlag", true);
            Long loginCounts = authLogService.countLoginByParams(params1);
            mv.addObject("data_total", loginCounts);
            // 电教馆累计登录人次
            Map<String, Object> params2 = new HashMap<>();
            params2.put("authSource", "上海运营中心");
            params2.put("getUserInfoSuccessFlag", true);
            params2.put("oauthType", OauthType.dianjiaoguan.name());
            Long dianjiaoguanLoginCounts = authLogService.countLoginByParams(params2);
            mv.addObject("data_total_djg", dianjiaoguanLoginCounts);
            // 省运营累计登录人次
            Map<String, Object> params3 = new HashMap<>();
            params3.put("authSource", "上海运营中心");
            params3.put("getUserInfoSuccessFlag", true);
            params3.put("oauthType", OauthType.edenoperation.name());
            Long edenoperationLoginCounts = authLogService.countLoginByParams(params3);
            mv.addObject("data_total_syy", edenoperationLoginCounts);
            // 近30日累计登录人次
            Map<String, Object> params4 = new HashMap<>();
            params4.put("authSource", "上海运营中心");
            params4.put("getUserInfoSuccessFlag", true);
            params4.put("beginTime", getZeroTime(DateUtils.addDays(new Date(), -29)));
            Long loginCounts30Day = authLogService.countLoginByParams(params4);
            mv.addObject("data_30", loginCounts30Day);
            // 电教馆近30日累计登录人次
            Map<String, Object> params5 = new HashMap<>();
            params5.put("authSource", "上海运营中心");
            params5.put("getUserInfoSuccessFlag", true);
            params5.put("oauthType", OauthType.dianjiaoguan.name());
            params5.put("beginTime", getZeroTime(DateUtils.addDays(new Date(), -29)));
            Long dianjiaoguanLoginCounts30Day = authLogService.countLoginByParams(params5);
            mv.addObject("data_30_djg", dianjiaoguanLoginCounts30Day);
            // 省运营近30日累计登录人次
            Map<String, Object> params6 = new HashMap<>();
            params6.put("authSource", "上海运营中心");
            params6.put("getUserInfoSuccessFlag", true);
            params6.put("oauthType", OauthType.edenoperation.name());
            params6.put("beginTime", getZeroTime(DateUtils.addDays(new Date(), -29)));
            Long edenoperationLoginCounts30Day = authLogService.countLoginByParams(params6);
            mv.addObject("data_30_syy", edenoperationLoginCounts30Day);
            // 近7日累计登录人次
            Map<String, Object> params7 = new HashMap<>();
            params7.put("authSource", "上海运营中心");
            params7.put("getUserInfoSuccessFlag", true);
            params7.put("beginTime", getZeroTime(DateUtils.addDays(new Date(), -6)));
            Long loginCounts7Day = authLogService.countLoginByParams(params7);
            mv.addObject("data_7", loginCounts7Day);
            // 电教馆近7日累计登录人次
            Map<String, Object> params8 = new HashMap<>();
            params8.put("authSource", "上海运营中心");
            params8.put("getUserInfoSuccessFlag", true);
            params8.put("oauthType", OauthType.dianjiaoguan.name());
            params8.put("beginTime", getZeroTime(DateUtils.addDays(new Date(), -6)));
            Long dianjiaoguanLoginCounts7Day = authLogService.countLoginByParams(params8);
            mv.addObject("data_7_djj", dianjiaoguanLoginCounts7Day);
            // 省运营近7日累计登录人次
            Map<String, Object> params9 = new HashMap<>();
            params9.put("authSource", "上海运营中心");
            params9.put("getUserInfoSuccessFlag", true);
            params9.put("oauthType", OauthType.edenoperation.name());
            params9.put("beginTime", getZeroTime(DateUtils.addDays(new Date(), -6)));
            Long edenoperationLoginCounts7Day = authLogService.countLoginByParams(params9);
            mv.addObject("data_7_syy", edenoperationLoginCounts7Day);
            // 近7日登录人次查询
            List<String> dates = new ArrayList<>();
            List<Long> dianjiaoguanDay7counts = new ArrayList<>();
            List<Long> edenoperationDay7counts = new ArrayList<>();
            for(int i = 0;i<7;i++){
                dates.add(getDateDtr(DateUtils.addDays(new Date(), -(6-i))));
                Map<String, Object> params = new HashMap<>();
                params.put("authSource", "上海运营中心");
                params.put("getUserInfoSuccessFlag", true);
                params.put("beginTime", getZeroTime(DateUtils.addDays(new Date(), -(6-i))));
                params.put("endTime", getEndTime(DateUtils.addDays(new Date(), -(6-i))));
                params.put("oauthType", OauthType.dianjiaoguan.name());
                Long dianjiaoguanCounts = authLogService.countLoginByParams(params);
                dianjiaoguanDay7counts.add(dianjiaoguanCounts);
                params.put("oauthType", OauthType.edenoperation.name());
                Long edenoperationCounts = authLogService.countLoginByParams(params);
                edenoperationDay7counts.add(edenoperationCounts);
            }
            mv.addObject("data_time", dates);
            mv.addObject("data_djg", dianjiaoguanDay7counts);
            mv.addObject("data_syy", edenoperationDay7counts);
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
