package cn.adcc.authorization.dao;

import cn.adcc.authorization.model.SysPermission;

import java.util.List;

public interface SysPermissionRepository extends BaseRepository<SysPermission> {
    List<SysPermission> findAllByOrderBySortAsc();

    List<SysPermission> findAllByType(Integer type);
}
