package cn.adcc.authorization.aware;

import cn.adcc.authorization.utils.SecurityUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class UsernameAuditorBean implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        String username = SecurityUtils.getUsername();
        return Optional.ofNullable(username);
        //return Optional.of(username);
    }
}
