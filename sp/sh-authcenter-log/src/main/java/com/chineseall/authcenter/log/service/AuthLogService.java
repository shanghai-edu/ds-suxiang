package com.chineseall.authcenter.log.service;



import com.chineseall.authcenter.log.model.AuthLog;

import java.util.Map;

public interface AuthLogService {

    AuthLog getById(String id);

    void save(AuthLog authLog);

    Long countLoginByParams(Map<String, Object> params);

    Long countLogoutByParams(Map<String, Object> params);
}
