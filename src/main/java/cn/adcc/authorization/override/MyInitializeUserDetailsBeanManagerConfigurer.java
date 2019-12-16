package cn.adcc.authorization.override;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(2147478646)
public class MyInitializeUserDetailsBeanManagerConfigurer extends GlobalAuthenticationConfigurerAdapter {
    static final int DEFAULT_ORDER = 2147478646;
    private final ApplicationContext context;

    MyInitializeUserDetailsBeanManagerConfigurer(ApplicationContext context) {
        this.context = context;
    }

    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.apply(new MyInitializeUserDetailsBeanManagerConfigurer.InitializeUserDetailsManagerConfigurer());
    }

    class InitializeUserDetailsManagerConfigurer extends GlobalAuthenticationConfigurerAdapter {
        InitializeUserDetailsManagerConfigurer() {
        }

        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            if (!auth.isConfigured()) {
                UserDetailsService userDetailsService = (UserDetailsService)this.getBeanOrNull(UserDetailsService.class);
                if (userDetailsService != null) {
                    PasswordEncoder passwordEncoder = (PasswordEncoder)this.getBeanOrNull(PasswordEncoder.class);
                    UserDetailsPasswordService passwordManager = (UserDetailsPasswordService)this.getBeanOrNull(UserDetailsPasswordService.class);
                    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                    provider.setUserDetailsService(userDetailsService);
                    provider.setHideUserNotFoundExceptions(false);
                    if (passwordEncoder != null) {
                        provider.setPasswordEncoder(passwordEncoder);
                    }

                    if (passwordManager != null) {
                        provider.setUserDetailsPasswordService(passwordManager);
                    }

                    provider.afterPropertiesSet();
                    auth.authenticationProvider(provider);
                }
            }
        }

        private <T> T getBeanOrNull(Class<T> type) {
            String[] userDetailsBeanNames = MyInitializeUserDetailsBeanManagerConfigurer.this.context.getBeanNamesForType(type);
            return userDetailsBeanNames.length != 1 ? null : MyInitializeUserDetailsBeanManagerConfigurer.this.context.getBean(userDetailsBeanNames[0], type);
        }
    }
}
