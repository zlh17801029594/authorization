package cn.adcc.authorization.service.serviceImpl;

import cn.adcc.authorization.model.SysClientDetails;
import cn.adcc.authorization.service.SysClientDetailsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class SysClientDetailsServiceImpl implements SysClientDetailsService {
    @Override
    public SysClientDetails findById(Long id) {
        return null;
    }

    @Override
    public List<SysClientDetails> findAll() {
        return null;
    }

    @Override
    public Page<SysClientDetails> findAll(PageRequest pageRequest) {
        return null;
    }

    @Override
    public SysClientDetails save(SysClientDetails sysClientDetails) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void delete(List<Long> ids) {

    }
}
