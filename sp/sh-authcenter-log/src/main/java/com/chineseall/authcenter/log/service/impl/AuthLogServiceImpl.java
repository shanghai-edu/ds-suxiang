package com.chineseall.authcenter.log.service.impl;

import com.chineseall.authcenter.log.dao.AuthLogDao;
import com.chineseall.authcenter.log.model.AuthLog;
import com.chineseall.authcenter.log.service.AuthLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

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
}
