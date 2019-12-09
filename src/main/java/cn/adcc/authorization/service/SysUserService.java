package cn.adcc.authorization.service;

import cn.adcc.authorization.model.SysUser;

public interface SysUserService extends BaseService<SysUser> {
    //4. findByUsername感觉是管理员的接口。findInfo更合适(用户中心详情页)，不在controller传递username？
    SysUser findByUsername(String username);

    /*2.个人修改和管理员修改逻辑不一样，考虑(1)从Authentication中取name，(2)传递整个SysUser。*/
    //用户更改密码
    void changePassword(String oldPassword, String newPassword);

    /*8.上传头像， 头像url字段写入userDetails(继承user实现)*/
    //void changeHeadImage();

    SysUser saveUser(SysUser sysUser);

    //5.考虑管理员 更改用户状态，重置密码接口统一，统一销毁token等
    //管理员重置密码， 可以设置成和用户锁定类似的按钮，放置在用户列表页，方便重置
    SysUser updatePassword(SysUser sysUser);

    void lockUser(SysUser sysUser);

    void disableUser(SysUser sysUser);
}
