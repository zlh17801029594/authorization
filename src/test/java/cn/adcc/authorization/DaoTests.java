package cn.adcc.authorization;

import cn.adcc.authorization.dao.SysUserRepository;
import cn.adcc.authorization.model.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
public class DaoTests {
    @Autowired
    private SysUserRepository sysUserRepository;

    @Test
    void testUserRepository() {
        List<SysUser> sysUserList = sysUserRepository.findAll();
        System.out.println(sysUserList.size());
        sysUserList.get(0).getGroups().get(0);
        //System.out.println(sysUserList.get(0).getGroups().get(0).getName());
        //System.out.println(sysUserRepository.findAll());
    }
}
