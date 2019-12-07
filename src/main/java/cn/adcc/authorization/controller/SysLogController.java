package cn.adcc.authorization.controller;

import cn.adcc.authorization.model.SysLog;
import cn.adcc.authorization.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @GetMapping("/log/{id}")
    public SysLog getLog(@PathVariable Long id) {
        return sysLogService.findById(id);
    }

}
