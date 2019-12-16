package cn.adcc.authorization.service.serviceImpl;

import cn.adcc.authorization.model.SysPermission;
import cn.adcc.authorization.service.SysPermissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class SysPermissionServiceImpl implements SysPermissionService {
    @Override
    public List<SysPermission> getPermissionList() {
        return null;
    }

    @Override
    public List<SysPermission> getPermissions(Integer type) {
        return null;
    }

    @Override
    public SysPermission findById(Long id) {
        return null;
    }

    @Override
    public List<SysPermission> findAll() {
        return null;
    }

    @Override
    public Page<SysPermission> findAll(PageRequest pageRequest) {
        return null;
    }

    @Override
    public SysPermission save(SysPermission sysPermission) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void delete(List<Long> ids) {

    }
}
