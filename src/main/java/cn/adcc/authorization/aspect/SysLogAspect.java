package cn.adcc.authorization.aspect;

import cn.adcc.authorization.model.SysLog;
import cn.adcc.authorization.service.SysLogService;
import cn.adcc.authorization.utils.HttpUtils;
import cn.adcc.authorization.utils.IPUtils;
import cn.adcc.authorization.utils.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("execution(* cn.adcc.authorization.service.*.*(..))")
    public void logPointcut() {

    }

    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = pjp.proceed();
        long time = System.currentTimeMillis() - beginTime;
        saveSysLog(pjp, time);
        return result;
    }

    private void saveSysLog(ProceedingJoinPoint pjp, long time) {
        if (pjp.getTarget() instanceof SysLogService) {
            return;
        }
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        SysLog sysLog = new SysLog();
        String className = pjp.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");
        Object[] args = pjp.getArgs();
        try {
            String params = objectMapper.writeValueAsString(args);
            if (params.length() > 200) {
                params = params.substring(0, 200) + "...";
            }
            sysLog.setParams(params);
        } catch (JsonProcessingException e) {
            sysLog.setParams("参数序列化异常");
        }
        HttpServletRequest request = HttpUtils.getHttpServletRequest();
        sysLog.setIp(IPUtils.getIpAddr(request));
        String username = SecurityUtils.getUsername();
        sysLog.setUsername(username);
        sysLog.setTime(time);
        sysLogService.save(sysLog);
    }
}
