package cn.adcc.authorization.config;

import cn.adcc.authorization.constants.SecurityConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"authorizationRequest"})
public class PageConfig {

    @GetMapping(SecurityConstants.LOGIN_PAGE)
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String page() {
        return "index";
    }

    @GetMapping("/oauth/confirm_access")
    public String confirm() {
        return "authorize";
    }
}
