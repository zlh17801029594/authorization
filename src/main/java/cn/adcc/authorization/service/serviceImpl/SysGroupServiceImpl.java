package cn.adcc.authorization.service.serviceImpl;

import cn.adcc.authorization.model.SysGroup;
import cn.adcc.authorization.service.SysGroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysGroupServiceImpl implements SysGroupService {

    @Override
    public SysGroup findById(Long id) {
        return null;
    }

    @Override
    public List<SysGroup> findAll() {
        return null;
    }

    @Override
    public Page<SysGroup> findAll(PageRequest pageRequest) {
        return null;
    }

    @Override
    public SysGroup save(SysGroup sysGroup) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void delete(List<Long> ids) {

    }
}
