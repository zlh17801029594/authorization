package cn.adcc.authorization.service.serviceImpl;

import cn.adcc.authorization.dao.SysLogMapper;
import cn.adcc.authorization.model.SysLog;
import cn.adcc.authorization.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void save(SysLog record) {
        sysLogMapper.insert(record);
    }

    @Override
    public void delete(Long id) {
    }

    @Override
    public void delete(List<Long> ids) {
    }

    @Override
    public void update(SysLog record) {
    }

    @Override
    public SysLog findById(Long id) {
        return sysLogMapper.findById(id);
    }

    @Override
    public List<SysLog> findAll() {
        return null;
    }
}
