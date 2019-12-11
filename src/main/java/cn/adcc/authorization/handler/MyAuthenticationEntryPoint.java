package cn.adcc.authorization.handler;

import cn.adcc.authorization.utils.ResponseUtil;
import cn.adcc.authorization.utils.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        String errorMsg = "请先登录";
        if (e != null) {
            errorMsg = e.getMessage();
        }
        ResponseUtil.response(response, ResultUtil.failure("401", errorMsg));
    }
}
