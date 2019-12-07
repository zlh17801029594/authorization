package cn.adcc.authorization.dao;

import cn.adcc.authorization.model.SysLog;

import java.util.List;

public interface SysLogMapper {

    int insert(SysLog sysLog);

    int delete(Long id);

    int delete(List<Long> ids);

    SysLog findById(Long id);

    List<SysLog> findAll();
}
