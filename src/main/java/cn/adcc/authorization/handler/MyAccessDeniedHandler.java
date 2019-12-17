package cn.adcc.authorization.handler;

import cn.adcc.authorization.utils.ResponseUtil;
import cn.adcc.authorization.utils.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    private String errorPage;

    public void setErrorPage(String errorPage) {
        if (errorPage != null && !errorPage.startsWith("/")) {
            throw new IllegalArgumentException("errorPage must begin with '/'");
        } else {
            this.errorPage = errorPage;
        }
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        if (!response.isCommitted()) {
            if (this.errorPage != null) {
                request.setAttribute("SPRING_SECURITY_403_EXCEPTION", exception);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                RequestDispatcher dispatcher = request.getRequestDispatcher(this.errorPage);
                dispatcher.forward(request, response);
            } else {
                response.sendError(HttpStatus.FORBIDDEN.value(), exception.getMessage());
            }
        }
    }
}
