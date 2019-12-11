package cn.adcc.authorization.config;

import cn.adcc.authorization.constants.SecurityConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageConfig {

    @GetMapping(SecurityConstants.LOGIN_PAGE)
    public String login() {
        return "login";
    }
}
