package cn.adcc.authorization.handler;

import cn.adcc.authorization.utils.ResponseUtil;
import cn.adcc.authorization.utils.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        if (savedRequest == null) {
            System.out.println(String.format("无savedRequest"));
            this.clearAuthenticationAttributes(request);
            ResponseUtil.response(response, ResultUtil.success());
        } else {
            String targetUrlParameter = this.getTargetUrlParameter();
            if (!this.isAlwaysUseDefaultTargetUrl() && (targetUrlParameter == null || !StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
                System.out.println(String.format("使用savedRequest"));
                this.clearAuthenticationAttributes(request);
                String targetUrl = savedRequest.getRedirectUrl();
                ResponseUtil.response(response, ResultUtil.success(targetUrl));
            } else {
                System.out.println(String.format("有savedRequest，未使用"));
                this.requestCache.removeRequest(request, response);
                this.clearAuthenticationAttributes(request);
                ResponseUtil.response(response, ResultUtil.success());
            }
        }
    }
}
