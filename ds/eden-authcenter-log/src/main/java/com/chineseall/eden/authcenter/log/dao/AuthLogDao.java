package com.chineseall.eden.authcenter.log.dao;

import cn.sh.chineseall.framework.core.repackaged.org.springframework.data.domain.PageRequest;
import cn.sh.chineseall.framework.core.repackaged.org.springframework.data.domain.Pageable;
import cn.sh.chineseall.framework.core.repackaged.org.springframework.data.domain.Sort;
import cn.sh.chineseall.framework.dao.core.hql.Criteria;
import cn.sh.chineseall.framework.dao.core.hql.Query;
import cn.sh.chineseall.framework.dao.mongo.dao.StaticCacheDimensionDocumentMongoDao;
import com.chineseall.eden.authcenter.log.model.AuthLog;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
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

    public List<String> distinct(String key, Map<String, Object> params){
        Criteria criteria = new Criteria();
        if (null != params && params.size() > 0){
            params.forEach((k,v)->{
                criteria.and(k).is(v);
            });
        }
        return super.distinct(key,new Query(criteria),String.class);
    }

    public long count(Map<String, Object> params){
        Criteria criteria = new Criteria();
        if (null != params && params.size() > 0){
            params.forEach((k,v)->{
                criteria.and(k).is(v);
            });
        }
        Query query = Query.query(criteria);
        return count(query);
    }

    public List<AuthLog> listLogWithPage(int pageNo, int pageSize,Map<String, Object> prams) {
        Criteria criteria = new Criteria();
        if (null != prams && prams.size() > 0){
            prams.forEach((k,v)->{
                criteria.and(k).is(v);
            });
        }
        Query query = new Query(criteria);
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(pageNo-1, pageSize, sort);
        return query(query.with(pageable));
    }
}
