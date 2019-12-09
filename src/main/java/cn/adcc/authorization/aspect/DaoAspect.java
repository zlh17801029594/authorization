package cn.adcc.authorization.aspect;

import cn.adcc.authorization.utils.SecurityUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/*@Aspect
@Component
@Configuration*/
public class DaoAspect {
    private static final String createTime = "createTime";
    private static final String updateTime = "updateTime";
    private static final String createBy = "createBy";
    private static final String updateBy = "updateBy";

    @Pointcut("execution(* cn.adcc.authorization.dao.*.insert*(..))")
    public void daoCreate() {

    }

    @Pointcut("execution(* cn.adcc.authorization.dao.*.update*(..))")
    public void daoUpdate() {

    }

    @Around("daoCreate()")
    public Object doAroundCreate(ProceedingJoinPoint pjp) throws Throwable {
        return around(pjp, createBy, createTime);
    }

    @Around("daoUpdate()")
    public Object doAroundUpdate(ProceedingJoinPoint pjp) throws Throwable {
        return around(pjp, updateBy, updateTime);
    }

    private Object around(ProceedingJoinPoint pjp, String property1, String property2) throws Throwable {
        Object[] objects = pjp.getArgs();
        if (objects != null && objects.length > 0) {
            for (Object arg : objects) {
                try{
                    /*1.有没有更好的方式替换？*/
                    BeanUtils.getProperty(arg, property1);
                    String username = getUsername();
                    if (username != null) {
                        BeanUtils.setProperty(arg, property1, username);
                    }
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                } catch (NoSuchMethodException e) {
                }
                try{
                    BeanUtils.getProperty(arg, property2);
                    BeanUtils.setProperty(arg, property2, new Date());
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                } catch (NoSuchMethodException e) {
                }
            }
        }
        return pjp.proceed();
    }

    private String getUsername() {
        return SecurityUtils.getUsername();
    }
}
