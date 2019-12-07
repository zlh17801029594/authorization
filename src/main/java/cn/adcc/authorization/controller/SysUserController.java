package cn.adcc.authorization.controller;

import cn.adcc.authorization.service.SysUserService;
import cn.adcc.authorization.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/user")
    public String getInfo() {
        sysUserService.save();
        return SecurityUtils.getUsername();
    }
}
