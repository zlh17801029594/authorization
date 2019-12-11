package cn.adcc.authorization.handler;

import cn.adcc.authorization.utils.ResponseUtil;
import cn.adcc.authorization.utils.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        String errorMsg = "认证失败";
        if (e != null) {
            if (e instanceof BadCredentialsException) {
                errorMsg = "密码错误";
            } else if (e instanceof DisabledException) {
                errorMsg = "当前用户已被禁用";
            } else {
                errorMsg = e.getMessage();
            }
        }
        ResponseUtil.response(response, ResultUtil.failure(errorMsg));
    }
}
