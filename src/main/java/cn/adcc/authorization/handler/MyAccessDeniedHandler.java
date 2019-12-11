package cn.adcc.authorization.handler;

import cn.adcc.authorization.utils.ResponseUtil;
import cn.adcc.authorization.utils.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        String errorMsg = "权限不足";
        if (e != null) {
            errorMsg = e.getMessage();
        }
        ResponseUtil.response(response, ResultUtil.failure("403", errorMsg));
    }
}
