package cn.adcc.authorization;

import cn.adcc.authorization.model.SysUser;
import cn.adcc.authorization.utils.BeanPropertiesCopyUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.Optional;

public class CommonsTests {

    @Test
    void testThrows() {
        Optional<String> emptyOpt = Optional.empty();
        assertThrows(NoSuchElementException.class,
                () -> emptyOpt.get());
        assertThrows(NullPointerException.class,
                () -> Optional.of(null));
        assertDoesNotThrow(() -> {
            Optional.ofNullable(null);
        });
    }

    @Test
    void TestEquals() {
        Optional<String> opt = Optional.ofNullable("ming");
        assertTrue(opt.isPresent());
        assertEquals("ming", opt.get());
        opt.ifPresent(o -> assertEquals("ming", o));
    }

    @Test
    void TestMap() {
        Optional<String> opt = Optional.ofNullable(null);
        //opt.get();
        //opt.filter(o -> "s".equals(o));
        System.out.println(opt.filter(o -> o.equals("s")));
    }

    @Test
    void TestBeanPropertiesUtils() {
        SysUser sysUser1 = new SysUser();
        sysUser1.setNickname("zlh");
        sysUser1.setPassword("123");
        System.out.println(sysUser1);
        SysUser sysUser2 = new SysUser();
        sysUser2.setNickname("mmm");
        sysUser2.setPassword("456");
        System.out.println(sysUser2);
        BeanPropertiesCopyUtils.copyNotNull(sysUser1, sysUser2, "password, client_secret");
        System.out.println(sysUser1);
    }
}
