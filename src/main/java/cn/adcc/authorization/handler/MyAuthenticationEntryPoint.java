package cn.adcc.authorization.handler;

import cn.adcc.authorization.utils.ResponseUtil;
import cn.adcc.authorization.utils.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private String errorPage;

    public void setErrorPage(String errorPage) {
        if (errorPage != null && !errorPage.startsWith("/")) {
            throw new IllegalArgumentException("errorPage must begin with '/'");
        } else {
            this.errorPage = errorPage;
        }
    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("处理异常：" + exception.getMessage());
        if (!response.isCommitted()) {
            if (this.errorPage != null) {
                request.setAttribute("SPRING_SECURITY_401_EXCEPTION", exception);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                RequestDispatcher dispatcher = request.getRequestDispatcher(this.errorPage);
                dispatcher.forward(request, response);
            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
            }
        }
    }
}
