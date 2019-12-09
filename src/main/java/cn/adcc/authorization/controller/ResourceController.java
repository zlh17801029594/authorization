package cn.adcc.authorization.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ResourceController {

    @RequestMapping("/info")
    public Principal principal(Principal principal) {
        return principal;
    }
}
