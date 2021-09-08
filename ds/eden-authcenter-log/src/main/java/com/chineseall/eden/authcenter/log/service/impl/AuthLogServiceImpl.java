package com.chineseall.eden.authcenter.log.service.impl;

import cn.sh.chineseall.framework.core.util.CollectionUtils;
import com.chineseall.eden.authcenter.log.dao.AuthLogDao;
import com.chineseall.eden.authcenter.log.model.AuthLog;
import com.chineseall.eden.authcenter.log.service.AuthLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthLogServiceImpl implements AuthLogService {

    @Autowired
    private AuthLogDao authLogDao;

    @Override
    public AuthLog getById(String id) {
        return authLogDao.load(id);
    }

    @Override
    public void save(AuthLog authLog) {
        if(authLog.getId() != null){
            authLog.setUpdateTime(new Date());
            authLogDao.replace(authLog);
        } else{
            authLog.setId(UUID.randomUUID().toString());
            authLog.setCreateTime(new Date());
            authLogDao.insert(authLog);
        }
    }

    @Override
    public Long countLoginByParams(Map<String, Object> params) {
        return authLogDao.countLoginByParams(params);
    }

    @Override
    public Long countLogoutByParams(Map<String, Object> params) {
        return authLogDao.countLogoutByParams(params);
    }

    @Override
    public List<String> distinct(String key, Map<String, Object> prams) {
        List<String> stringList = authLogDao.distinct(key, prams);
        List<String> result=new ArrayList<>();
        if (CollectionUtils.isNotEmpty(stringList)){
            stringList.forEach(item -> {
                if (null != item){
                    result.add(item);
                }
            });
        }

        return result;
    }

    @Override
    public long count(Map<String, Object> prams) {
        return authLogDao.count(prams);
    }

    @Override
    public void replace(AuthLog authLog) {
        authLogDao.replace(authLog);
    }

    @Override
    public List<AuthLog> listLogWithPage(int pageNo, int pageSize,Map<String, Object> prams) {
        return authLogDao.listLogWithPage(pageNo,pageSize,prams);
    }
}
