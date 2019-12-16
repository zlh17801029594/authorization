package cn.adcc.authorization.page;

import cn.adcc.authorization.model.SysUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @RequestMapping("/a1")
    public ModelAndView test1() {
        Map<String, Object> map = new HashMap<>();
        SysUser sysUser = new SysUser();
        sysUser.setUsername("小周");
        map.put("test", sysUser);
        return new ModelAndView("a1", map);
    }

    @RequestMapping("/a2")
    public ModelAndView test2() {
        Map<String, Object> map = new HashMap<>();
        SysUser sysUser = new SysUser();
        sysUser.setUsername("小周");
        map.put("test", sysUser);
        return new ModelAndView("forward:/a3", map);
    }

    @RequestMapping("/a3")
    public ModelAndView test3() {
        return new ModelAndView("a3");
    }
}
