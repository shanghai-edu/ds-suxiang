package com.chineseall.eden.authcenter.log.service;

import com.chineseall.eden.authcenter.log.model.AuthLog;

import java.util.List;
import java.util.Map;

public interface AuthLogService {

    AuthLog getById(String id);

    void save(AuthLog authLog);

    Long countLoginByParams(Map<String, Object> params);

    Long countLogoutByParams(Map<String, Object> params);

    List<String> distinct(String key, Map<String, Object> prams);

    long count(Map<String,Object> prams);

    void replace(AuthLog authLog);

    public List<AuthLog> listLogWithPage(int pageNo, int pageSize,Map<String, Object> prams);
}
