package cn.adcc.authorization;

import cn.adcc.authorization.controller.SysUserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ControllerTests {
    @Autowired
    private SysUserController sysUserController;

    @Test
    void testUserController() {
        sysUserController.getInfo();
        //System.out.println(sysUserController.getInfo());
    }
}
