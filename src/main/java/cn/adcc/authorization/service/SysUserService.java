package cn.adcc.authorization.service;

import cn.adcc.authorization.model.SysUser;

public interface SysUserService extends CurdService<SysUser> {
    void save();

    void changePassword(String oldPassword, String newPassword);

    boolean userExists(String username);


}
