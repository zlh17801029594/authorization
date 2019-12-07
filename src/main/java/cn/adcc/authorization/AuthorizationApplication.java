package cn.adcc.authorization;

import cn.adcc.authorization.model.BaseModel;
import cn.adcc.authorization.model.Result;
import cn.adcc.authorization.model.SysRole;
import cn.adcc.authorization.model.SysUser;
import org.apache.commons.beanutils.BeanUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.InvocationTargetException;

@SpringBootApplication
@MapperScan("cn.adcc.authorization.dao")
public class AuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}
}
