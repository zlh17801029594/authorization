package cn.adcc.authorization.dao;

import cn.adcc.authorization.model.SysUser;

public interface SysUserRepository extends BaseRepository<SysUser> {
    SysUser findByUsername(String username);
}
