package cn.adcc.authorization.controller;

import cn.adcc.authorization.model.SysUser;
import cn.adcc.authorization.service.SysUserService;
import cn.adcc.authorization.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/user")
    public List<SysUser> getInfo() {
        List<SysUser> sysUsers = sysUserService.findAll();
        return sysUsers;
    }

    @PostMapping("/user")
    public SysUser save(@RequestBody SysUser sysUser) {
        return sysUserService.save(sysUser);
    }
}
