package cn.adcc.authorization.service.serviceImpl;

import cn.adcc.authorization.model.SysUser;
import cn.adcc.authorization.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Override
    public void save() {
        System.out.println("hello world");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public void save(SysUser record) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void delete(List<Long> ids) {

    }

    @Override
    public void update(SysUser record) {

    }

    @Override
    public SysUser findById(Long id) {
        return null;
    }

    @Override
    public List<SysUser> findAll() {
        return null;
    }
}
