package cn.adcc.authorization.override;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;

import cn.adcc.authorization.page.AuthorizationController;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;
import org.springframework.security.oauth2.provider.endpoint.RedirectResolver;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenKeyEndpoint;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelErrorEndpoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

@Configuration
@Import({AuthorizationServerEndpointsConfiguration.TokenKeyEndpointRegistrar.class})
public class AuthorizationServerEndpointsConfiguration {
    private AuthorizationServerEndpointsConfigurer endpoints = new AuthorizationServerEndpointsConfigurer();
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private List<AuthorizationServerConfigurer> configurers = Collections.emptyList();

    public AuthorizationServerEndpointsConfiguration() {
    }

    @PostConstruct
    public void init() {
        Iterator var1 = this.configurers.iterator();

        while(var1.hasNext()) {
            AuthorizationServerConfigurer configurer = (AuthorizationServerConfigurer)var1.next();

            try {
                configurer.configure(this.endpoints);
            } catch (Exception var4) {
                throw new IllegalStateException("Cannot configure enpdoints", var4);
            }
        }

        this.endpoints.setClientDetailsService(this.clientDetailsService);
    }

    @Bean
    public AuthorizationEndpoint authorizationEndpoint() throws Exception {
        AuthorizationEndpoint authorizationEndpoint = new AuthorizationEndpoint();
        FrameworkEndpointHandlerMapping mapping = this.getEndpointsConfigurer().getFrameworkEndpointHandlerMapping();
        authorizationEndpoint.setUserApprovalPage(this.extractPath(mapping, "/oauth/confirm_access"));
        authorizationEndpoint.setProviderExceptionHandler(this.exceptionTranslator());
        authorizationEndpoint.setErrorPage(this.extractPath(mapping, "/oauth/error"));
        authorizationEndpoint.setTokenGranter(this.tokenGranter());
        authorizationEndpoint.setClientDetailsService(this.clientDetailsService);
        authorizationEndpoint.setAuthorizationCodeServices(this.authorizationCodeServices());
        authorizationEndpoint.setOAuth2RequestFactory(this.oauth2RequestFactory());
        authorizationEndpoint.setOAuth2RequestValidator(this.oauth2RequestValidator());
        authorizationEndpoint.setUserApprovalHandler(this.userApprovalHandler());
        authorizationEndpoint.setRedirectResolver(this.redirectResolver());
        return authorizationEndpoint;
    }

    @Bean
    public AuthorizationController authorizationController() throws Exception {
        AuthorizationController authorizationController = new AuthorizationController();
        FrameworkEndpointHandlerMapping mapping = this.getEndpointsConfigurer().getFrameworkEndpointHandlerMapping();
        authorizationController.setUserApprovalPage(this.extractPath(mapping, "/oauth/confirm_access"));
        authorizationController.setProviderExceptionHandler(this.exceptionTranslator());
        authorizationController.setErrorPage(this.extractPath(mapping, "/oauth/error"));
        authorizationController.setTokenGranter(this.tokenGranter());
        authorizationController.setClientDetailsService(this.clientDetailsService);
        authorizationController.setAuthorizationCodeServices(this.authorizationCodeServices());
        authorizationController.setOAuth2RequestFactory(this.oauth2RequestFactory());
        authorizationController.setOAuth2RequestValidator(this.oauth2RequestValidator());
        authorizationController.setUserApprovalHandler(this.userApprovalHandler());
        authorizationController.setRedirectResolver(this.redirectResolver());
        return authorizationController;
    }

    @Bean
    public TokenEndpoint tokenEndpoint() throws Exception {
        TokenEndpoint tokenEndpoint = new TokenEndpoint();
        tokenEndpoint.setClientDetailsService(this.clientDetailsService);
        tokenEndpoint.setProviderExceptionHandler(this.exceptionTranslator());
        tokenEndpoint.setTokenGranter(this.tokenGranter());
        tokenEndpoint.setOAuth2RequestFactory(this.oauth2RequestFactory());
        tokenEndpoint.setOAuth2RequestValidator(this.oauth2RequestValidator());
        tokenEndpoint.setAllowedRequestMethods(this.allowedTokenEndpointRequestMethods());
        return tokenEndpoint;
    }

    @Bean
    public CheckTokenEndpoint checkTokenEndpoint() {
        CheckTokenEndpoint endpoint = new CheckTokenEndpoint(this.getEndpointsConfigurer().getResourceServerTokenServices());
        endpoint.setAccessTokenConverter(this.getEndpointsConfigurer().getAccessTokenConverter());
        endpoint.setExceptionTranslator(this.exceptionTranslator());
        return endpoint;
    }

    @Bean
    public WhitelabelApprovalEndpoint whitelabelApprovalEndpoint() {
        return new WhitelabelApprovalEndpoint();
    }

    @Bean
    public WhitelabelErrorEndpoint whitelabelErrorEndpoint() {
        return new WhitelabelErrorEndpoint();
    }

    @Bean
    public FrameworkEndpointHandlerMapping oauth2EndpointHandlerMapping() throws Exception {
        return this.getEndpointsConfigurer().getFrameworkEndpointHandlerMapping();
    }

    @Bean
    public FactoryBean<ConsumerTokenServices> consumerTokenServices() throws Exception {
        return new AbstractFactoryBean<ConsumerTokenServices>() {
            public Class<?> getObjectType() {
                return ConsumerTokenServices.class;
            }

            protected ConsumerTokenServices createInstance() throws Exception {
                return AuthorizationServerEndpointsConfiguration.this.getEndpointsConfigurer().getConsumerTokenServices();
            }
        };
    }

    @Bean
    public FactoryBean<AuthorizationServerTokenServices> defaultAuthorizationServerTokenServices() {
        return new AuthorizationServerEndpointsConfiguration.AuthorizationServerTokenServicesFactoryBean(this.endpoints);
    }

    public AuthorizationServerEndpointsConfigurer getEndpointsConfigurer() {
        if (!this.endpoints.isTokenServicesOverride()) {
            try {
                this.endpoints.tokenServices(this.endpoints.getDefaultAuthorizationServerTokenServices());
            } catch (Exception var2) {
                throw new BeanCreationException("Cannot create token services", var2);
            }
        }

        return this.endpoints;
    }

    private Set<HttpMethod> allowedTokenEndpointRequestMethods() {
        return this.getEndpointsConfigurer().getAllowedTokenEndpointRequestMethods();
    }

    private OAuth2RequestFactory oauth2RequestFactory() throws Exception {
        return this.getEndpointsConfigurer().getOAuth2RequestFactory();
    }

    private UserApprovalHandler userApprovalHandler() throws Exception {
        return this.getEndpointsConfigurer().getUserApprovalHandler();
    }

    private OAuth2RequestValidator oauth2RequestValidator() throws Exception {
        return this.getEndpointsConfigurer().getOAuth2RequestValidator();
    }

    private AuthorizationCodeServices authorizationCodeServices() throws Exception {
        return this.getEndpointsConfigurer().getAuthorizationCodeServices();
    }

    private WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator() {
        return this.getEndpointsConfigurer().getExceptionTranslator();
    }

    private RedirectResolver redirectResolver() {
        return this.getEndpointsConfigurer().getRedirectResolver();
    }

    private TokenGranter tokenGranter() throws Exception {
        return this.getEndpointsConfigurer().getTokenGranter();
    }

    private String extractPath(FrameworkEndpointHandlerMapping mapping, String page) {
        String path = mapping.getPath(page);
        return path.contains(":") ? path : "forward:" + path;
    }

    @Component
    protected static class TokenKeyEndpointRegistrar implements BeanDefinitionRegistryPostProcessor {
        private BeanDefinitionRegistry registry;

        protected TokenKeyEndpointRegistrar() {
        }

        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, JwtAccessTokenConverter.class, false, false);
            if (names.length > 0) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(TokenKeyEndpoint.class);
                builder.addConstructorArgReference(names[0]);
                this.registry.registerBeanDefinition(TokenKeyEndpoint.class.getName(), builder.getBeanDefinition());
            }

        }

        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            this.registry = registry;
        }
    }

    protected static class AuthorizationServerTokenServicesFactoryBean extends AbstractFactoryBean<AuthorizationServerTokenServices> {
        private AuthorizationServerEndpointsConfigurer endpoints;

        protected AuthorizationServerTokenServicesFactoryBean() {
        }

        public AuthorizationServerTokenServicesFactoryBean(AuthorizationServerEndpointsConfigurer endpoints) {
            this.endpoints = endpoints;
        }

        public Class<?> getObjectType() {
            return AuthorizationServerTokenServices.class;
        }

        protected AuthorizationServerTokenServices createInstance() throws Exception {
            return this.endpoints.getDefaultAuthorizationServerTokenServices();
        }
    }
}

