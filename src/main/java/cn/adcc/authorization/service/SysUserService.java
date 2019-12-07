package cn.adcc.authorization.service;

import cn.adcc.authorization.model.SysUser;

public interface SysUserService extends CurdService<SysUser> {
    void save();

    /*2.个人修改和管理员修改逻辑不一样，考虑(1)从Authentication中取name，(2)传递整个SysUser。*/
    void changePassword(String oldPassword, String newPassword);

    boolean userExists(String username);

    void disableUser();

    void lockUser();
}
