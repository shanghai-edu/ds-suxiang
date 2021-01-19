package com.chineseall.authcenter.log.dao;

import cn.sh.chineseall.framework.dao.core.hql.Criteria;
import cn.sh.chineseall.framework.dao.core.hql.Query;
import cn.sh.chineseall.framework.dao.mongo.dao.StaticCacheDimensionDocumentMongoDao;
import com.chineseall.authcenter.log.model.AuthLog;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class AuthLogDao extends StaticCacheDimensionDocumentMongoDao<AuthLog, String> {

    public Long countLoginByParams(Map<String, Object> params){
        Criteria criteria = new Criteria();
        criteria.and("logType").is("login");
        if(params.get("authSource")!=null){
            criteria.and("authSource").is(params.get("authSource").toString());
        }
        if(params.get("oauthType")!=null){
            criteria.and("oauthType").is(params.get("oauthType").toString());
        }
        if(params.get("loginSuccessFlag")!=null){
            criteria.and("loginSuccessFlag").is(Boolean.valueOf(params.get("loginSuccessFlag").toString()));
        }
        if(params.get("getUserInfoSuccessFlag")!=null){
            criteria.and("getUserInfoSuccessFlag").is(Boolean.valueOf(params.get("getUserInfoSuccessFlag").toString()));
        }
        if(params.get("userType")!=null){
            criteria.and("logUserInfo.userType").is(params.get("userType").toString());
        }
        if(params.get("beginTime")!=null){
            criteria.and("createTime").gte((Date) params.get("beginTime"));
        }
        if(params.get("endTime")!=null){
            criteria.lte((Date) params.get("endTime"));
        }
        Query query = Query.query(criteria);
        return count(query);
    }

    public Long countLogoutByParams(Map<String, Object> params){
        Criteria criteria = new Criteria();
        criteria.and("logType").is("logout");
        Query query = Query.query(criteria);
        return count(query);
    }
}
