package cn.adcc.authorization;

import cn.adcc.authorization.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;


@SpringBootTest
public class ServicesTests {
    @Autowired
    private SysUserService sysUserService;

    @Test
    void testUserService() {
        sysUserService.findAll();
        //System.out.println(sysUserService.findAll());
    }
}
