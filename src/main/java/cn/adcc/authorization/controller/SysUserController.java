package cn.adcc.authorization.controller;

import cn.adcc.authorization.model.SysUser;
import cn.adcc.authorization.service.SysUserService;
import cn.adcc.authorization.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/findAll")
    public List<SysUser> getInfo() {
        List<SysUser> sysUsers = sysUserService.findAll();
        return sysUsers;
    }

    @PostMapping
    public SysUser save(@RequestBody SysUser sysUser) {
        return sysUserService.save(sysUser);
    }

    @PostMapping("/nickname")
    public SysUser updateNickname(@RequestBody SysUser sysUser) {
        return sysUserService.saveUser(sysUser);
    }

    @PostMapping("/password")
    public SysUser updatePassword(@RequestBody SysUser sysUser) {
        return sysUserService.updatePassword(sysUser);
    }

    @PutMapping("/lock")
    public void lockedUser(@RequestBody SysUser sysUser) {
        try {
            sysUserService.lockUser(sysUser);
            if (!sysUser.getAccountNonLocked()) {
                //revoke 关于当前用户的token
            }
            //返回Response 成功 code, msg, data
        } catch (Exception e) {
            e.printStackTrace();
            //返回Response 失败 code，msg(异常原因)
        }
    }

    @GetMapping("/findByName")
    public SysUser findByUsername() {
        String username = SecurityUtils.getUsername();
        try {
            SysUser user = sysUserService.findByUsername(username);
            if (user != null) {
                return user;
            }
        } catch (Exception e) {
        }
        return null;
    }

    @PutMapping("/changePassword")
    public void changePassword(String oldPassword, String newPassword) {
        sysUserService.changePassword(oldPassword, newPassword);
    }
}
