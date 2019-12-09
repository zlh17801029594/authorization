package cn.adcc.authorization.service.serviceImpl;

import cn.adcc.authorization.dao.SysLogRepository;
import cn.adcc.authorization.model.SysLog;
import cn.adcc.authorization.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogRepository logRepository;

    @Override
    public SysLog findById(Long id) {
        return logRepository.findById(id).get();
    }

    @Override
    public List<SysLog> findAll() {
        return null;
    }

    @Override
    public Page<SysLog> findAll(PageRequest pageRequest) {
        return null;
    }

    @Override
    public SysLog save(SysLog sysLog) {
        System.out.println(sysLog);
        return logRepository.save(sysLog);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void delete(List<Long> ids) {

    }
}
