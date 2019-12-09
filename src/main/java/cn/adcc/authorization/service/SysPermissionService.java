package cn.adcc.authorization.service;

import cn.adcc.authorization.model.SysPermission;

import java.util.List;
import java.util.Set;

public interface SysPermissionService {
    List<SysPermission> getPermissionList();

    /*3.用户获取全部权限，动态菜单路由获取菜单权限，客户端获取操作权限*/
    List<SysPermission> getPermissions(Integer type);
}
