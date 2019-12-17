package cn.adcc.authorization.handler;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
@Order(-2147483647)
public class MyDefaultErrorAttributes extends DefaultErrorAttributes {

    public MyDefaultErrorAttributes() {
        super(false);
    }

    public MyDefaultErrorAttributes(boolean includeException) {
        super(includeException);
    }

    @Override
    public int getOrder() {
        return -2147483647;
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        Object status = errorAttributes.get("status");
        if (status.equals(404)) {
            errorAttributes.put("error", "访问页面不存在");
        } else if (status.equals(401)) {
            errorAttributes.put("error", "请先登录");
        } else if (status.equals(403)) {
            errorAttributes.put("error", "权限不足");
        } else if (status.equals(400)) {
            errorAttributes.put("error", "请求失败");
        } else if (status.equals(405)) {
            errorAttributes.put("error", "不支持该请求方式");
        } else if (status.equals(406)) {
            errorAttributes.put("error", "返回类型不支持");
        } else if (status.equals(415)) {
            errorAttributes.put("error", "接收参数类型错误");
        } else if (status.equals(500)) {
            errorAttributes.put("error", "服务端异常");
        }
        return errorAttributes;
    }
}
