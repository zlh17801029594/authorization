package cn.adcc.authorization.override;

import java.net.URI;
import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.AbstractEndpoint;
import org.springframework.security.oauth2.provider.endpoint.DefaultRedirectResolver;
import org.springframework.security.oauth2.provider.endpoint.RedirectResolver;
import org.springframework.security.oauth2.provider.implicit.ImplicitGrantService;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.DefaultSessionAttributeStore;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@SessionAttributes({"authorizationRequest", "org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST"})
public class AuthorizationController extends AbstractEndpoint {
    static final String AUTHORIZATION_REQUEST_ATTR_NAME = "authorizationRequest";
    static final String ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME = "org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST";
    private AuthorizationCodeServices authorizationCodeServices = new InMemoryAuthorizationCodeServices();
    private RedirectResolver redirectResolver = new DefaultRedirectResolver();
    private UserApprovalHandler userApprovalHandler = new DefaultUserApprovalHandler();
    private SessionAttributeStore sessionAttributeStore = new DefaultSessionAttributeStore();
    private OAuth2RequestValidator oauth2RequestValidator = new DefaultOAuth2RequestValidator();
    private String userApprovalPage = "forward:/oauth/confirm_access";
    private String errorPage = "forward:/oauth/error";
    private Object implicitLock = new Object();

    public AuthorizationController() {
    }

    public void setSessionAttributeStore(SessionAttributeStore sessionAttributeStore) {
        this.sessionAttributeStore = sessionAttributeStore;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    @RequestMapping({"/oauth/authorize"})
    public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters, SessionStatus sessionStatus, Principal principal) {
        AuthorizationRequest authorizationRequest = this.getOAuth2RequestFactory().createAuthorizationRequest(parameters);
        Set<String> responseTypes = authorizationRequest.getResponseTypes();
        if (!responseTypes.contains("token") && !responseTypes.contains("code")) {
            //throw new UnsupportedResponseTypeException("Unsupported response types: " + responseTypes);
            return new ModelAndView("oauth2Login");
        } else if (authorizationRequest.getClientId() == null) {
            throw new InvalidClientException("A client id must be provided");
        } else {
            try {
                if (principal instanceof Authentication && ((Authentication)principal).isAuthenticated()) {
                    ClientDetails client = this.getClientDetailsService().loadClientByClientId(authorizationRequest.getClientId());
                    String redirectUriParameter = (String)authorizationRequest.getRequestParameters().get("redirect_uri");
                    String resolvedRedirect = this.redirectResolver.resolveRedirect(redirectUriParameter, client);
                    if (!StringUtils.hasText(resolvedRedirect)) {
                        throw new RedirectMismatchException("A redirectUri must be either supplied or preconfigured in the ClientDetails");
                    } else {
                        authorizationRequest.setRedirectUri(resolvedRedirect);
                        this.oauth2RequestValidator.validateScope(authorizationRequest, client);
                        authorizationRequest = this.userApprovalHandler.checkForPreApproval(authorizationRequest, (Authentication)principal);
                        boolean approved = this.userApprovalHandler.isApproved(authorizationRequest, (Authentication)principal);
                        authorizationRequest.setApproved(approved);
                        if (authorizationRequest.isApproved()) {
                            if (responseTypes.contains("token")) {
                                return this.getImplicitGrantResponse(authorizationRequest);
                            }

                            if (responseTypes.contains("code")) {
                                return new ModelAndView(this.getAuthorizationCodeResponse(authorizationRequest, (Authentication)principal));
                            }
                        }

                        model.put("authorizationRequest", authorizationRequest);
                        model.put("org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST", this.unmodifiableMap(authorizationRequest));
                        return this.getUserApprovalPageResponse(model, authorizationRequest, (Authentication)principal);
                    }
                } else {
                    throw new InsufficientAuthenticationException("User must be authenticated with Spring Security before authorization can be completed.");
                }
            } catch (RuntimeException var11) {
                sessionStatus.setComplete();
                throw var11;
            }
        }
    }

    Map<String, Object> unmodifiableMap(AuthorizationRequest authorizationRequest) {
        Map<String, Object> authorizationRequestMap = new HashMap();
        authorizationRequestMap.put("client_id", authorizationRequest.getClientId());
        authorizationRequestMap.put("state", authorizationRequest.getState());
        authorizationRequestMap.put("redirect_uri", authorizationRequest.getRedirectUri());
        if (authorizationRequest.getResponseTypes() != null) {
            authorizationRequestMap.put("response_type", Collections.unmodifiableSet(new HashSet(authorizationRequest.getResponseTypes())));
        }

        if (authorizationRequest.getScope() != null) {
            authorizationRequestMap.put("scope", Collections.unmodifiableSet(new HashSet(authorizationRequest.getScope())));
        }

        authorizationRequestMap.put("approved", authorizationRequest.isApproved());
        if (authorizationRequest.getResourceIds() != null) {
            authorizationRequestMap.put("resourceIds", Collections.unmodifiableSet(new HashSet(authorizationRequest.getResourceIds())));
        }

        if (authorizationRequest.getAuthorities() != null) {
            authorizationRequestMap.put("authorities", Collections.unmodifiableSet(new HashSet(authorizationRequest.getAuthorities())));
        }

        return Collections.unmodifiableMap(authorizationRequestMap);
    }

    @RequestMapping(
            value = {"/oauth/authorize"},
            method = {RequestMethod.POST},
            params = {"user_oauth_approval"}
    )
    public View approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model, SessionStatus sessionStatus, Principal principal) {
        if (!(principal instanceof Authentication)) {
            sessionStatus.setComplete();
            throw new InsufficientAuthenticationException("User must be authenticated with Spring Security before authorizing an access token.");
        } else {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest)model.get("authorizationRequest");
            if (authorizationRequest == null) {
                sessionStatus.setComplete();
                throw new InvalidRequestException("Cannot approve uninitialized authorization request.");
            } else {
                Map<String, Object> originalAuthorizationRequest = (Map)model.get("org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST");
                if (this.isAuthorizationRequestModified(authorizationRequest, originalAuthorizationRequest)) {
                    throw new InvalidRequestException("Changes were detected from the original authorization request.");
                } else {
                    View var9;
                    try {
                        Set<String> responseTypes = authorizationRequest.getResponseTypes();
                        authorizationRequest.setApprovalParameters(approvalParameters);
                        authorizationRequest = this.userApprovalHandler.updateAfterApproval(authorizationRequest, (Authentication)principal);
                        boolean approved = this.userApprovalHandler.isApproved(authorizationRequest, (Authentication)principal);
                        authorizationRequest.setApproved(approved);
                        if (authorizationRequest.getRedirectUri() == null) {
                            sessionStatus.setComplete();
                            throw new InvalidRequestException("Cannot approve request when no redirect URI is provided.");
                        }

                        if (!authorizationRequest.isApproved()) {
                            RedirectView var13 = new RedirectView(this.getUnsuccessfulRedirect(authorizationRequest, new UserDeniedAuthorizationException("User denied access"), responseTypes.contains("token")), false, true, false);
                            return var13;
                        }

                        if (responseTypes.contains("token")) {
                            var9 = this.getImplicitGrantResponse(authorizationRequest).getView();
                            return var9;
                        }

                        var9 = this.getAuthorizationCodeResponse(authorizationRequest, (Authentication)principal);
                    } finally {
                        sessionStatus.setComplete();
                    }

                    return var9;
                }
            }
        }
    }

    private boolean isAuthorizationRequestModified(AuthorizationRequest authorizationRequest, Map<String, Object> originalAuthorizationRequest) {
        if (!ObjectUtils.nullSafeEquals(authorizationRequest.getClientId(), originalAuthorizationRequest.get("client_id"))) {
            return true;
        } else if (!ObjectUtils.nullSafeEquals(authorizationRequest.getState(), originalAuthorizationRequest.get("state"))) {
            return true;
        } else if (!ObjectUtils.nullSafeEquals(authorizationRequest.getRedirectUri(), originalAuthorizationRequest.get("redirect_uri"))) {
            return true;
        } else if (!ObjectUtils.nullSafeEquals(authorizationRequest.getResponseTypes(), originalAuthorizationRequest.get("response_type"))) {
            return true;
        } else if (!ObjectUtils.nullSafeEquals(authorizationRequest.getScope(), originalAuthorizationRequest.get("scope"))) {
            return true;
        } else if (!ObjectUtils.nullSafeEquals(authorizationRequest.isApproved(), originalAuthorizationRequest.get("approved"))) {
            return true;
        } else if (!ObjectUtils.nullSafeEquals(authorizationRequest.getResourceIds(), originalAuthorizationRequest.get("resourceIds"))) {
            return true;
        } else {
            return !ObjectUtils.nullSafeEquals(authorizationRequest.getAuthorities(), originalAuthorizationRequest.get("authorities"));
        }
    }

    private ModelAndView getUserApprovalPageResponse(Map<String, Object> model, AuthorizationRequest authorizationRequest, Authentication principal) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Loading user approval page: " + this.userApprovalPage);
        }

        model.putAll(this.userApprovalHandler.getUserApprovalRequest(authorizationRequest, principal));
        return new ModelAndView(this.userApprovalPage, model);
    }

    private ModelAndView getImplicitGrantResponse(AuthorizationRequest authorizationRequest) {
        try {
            TokenRequest tokenRequest = this.getOAuth2RequestFactory().createTokenRequest(authorizationRequest, "implicit");
            OAuth2Request storedOAuth2Request = this.getOAuth2RequestFactory().createOAuth2Request(authorizationRequest);
            OAuth2AccessToken accessToken = this.getAccessTokenForImplicitGrant(tokenRequest, storedOAuth2Request);
            if (accessToken == null) {
                throw new UnsupportedResponseTypeException("Unsupported response type: token");
            } else {
                return new ModelAndView(new RedirectView(this.appendAccessToken(authorizationRequest, accessToken), false, true, false));
            }
        } catch (OAuth2Exception var5) {
            return new ModelAndView(new RedirectView(this.getUnsuccessfulRedirect(authorizationRequest, var5, true), false, true, false));
        }
    }

    private OAuth2AccessToken getAccessTokenForImplicitGrant(TokenRequest tokenRequest, OAuth2Request storedOAuth2Request) {
        OAuth2AccessToken accessToken = null;
        Object var4 = this.implicitLock;
        synchronized(this.implicitLock) {
            accessToken = this.getTokenGranter().grant("implicit", new ImplicitTokenRequest(tokenRequest, storedOAuth2Request));
            return accessToken;
        }
    }

    private View getAuthorizationCodeResponse(AuthorizationRequest authorizationRequest, Authentication authUser) {
        try {
            return new RedirectView(this.getSuccessfulRedirect(authorizationRequest, this.generateCode(authorizationRequest, authUser)), false, true, false);
        } catch (OAuth2Exception var4) {
            return new RedirectView(this.getUnsuccessfulRedirect(authorizationRequest, var4, false), false, true, false);
        }
    }

    private String appendAccessToken(AuthorizationRequest authorizationRequest, OAuth2AccessToken accessToken) {
        Map<String, Object> vars = new LinkedHashMap();
        Map<String, String> keys = new HashMap();
        if (accessToken == null) {
            throw new InvalidRequestException("An implicit grant could not be made");
        } else {
            vars.put("access_token", accessToken.getValue());
            vars.put("token_type", accessToken.getTokenType());
            String state = authorizationRequest.getState();
            if (state != null) {
                vars.put("state", state);
            }

            Date expiration = accessToken.getExpiration();
            if (expiration != null) {
                long expires_in = (expiration.getTime() - System.currentTimeMillis()) / 1000L;
                vars.put("expires_in", expires_in);
            }

            String originalScope = (String)authorizationRequest.getRequestParameters().get("scope");
            if (originalScope == null || !OAuth2Utils.parseParameterList(originalScope).equals(accessToken.getScope())) {
                vars.put("scope", OAuth2Utils.formatParameterList(accessToken.getScope()));
            }

            Map<String, Object> additionalInformation = accessToken.getAdditionalInformation();
            Iterator var9 = additionalInformation.keySet().iterator();

            while(var9.hasNext()) {
                String key = (String)var9.next();
                Object value = additionalInformation.get(key);
                if (value != null) {
                    keys.put("extra_" + key, key);
                    vars.put("extra_" + key, value);
                }
            }

            return this.append(authorizationRequest.getRedirectUri(), vars, keys, true);
        }
    }

    private String generateCode(AuthorizationRequest authorizationRequest, Authentication authentication) throws AuthenticationException {
        try {
            OAuth2Request storedOAuth2Request = this.getOAuth2RequestFactory().createOAuth2Request(authorizationRequest);
            OAuth2Authentication combinedAuth = new OAuth2Authentication(storedOAuth2Request, authentication);
            String code = this.authorizationCodeServices.createAuthorizationCode(combinedAuth);
            return code;
        } catch (OAuth2Exception var6) {
            if (authorizationRequest.getState() != null) {
                var6.addAdditionalInformation("state", authorizationRequest.getState());
            }

            throw var6;
        }
    }

    private String getSuccessfulRedirect(AuthorizationRequest authorizationRequest, String authorizationCode) {
        if (authorizationCode == null) {
            throw new IllegalStateException("No authorization code found in the current request scope.");
        } else {
            Map<String, String> query = new LinkedHashMap();
            query.put("code", authorizationCode);
            String state = authorizationRequest.getState();
            if (state != null) {
                query.put("state", state);
            }

            return this.append(authorizationRequest.getRedirectUri(), query, false);
        }
    }

    private String getUnsuccessfulRedirect(AuthorizationRequest authorizationRequest, OAuth2Exception failure, boolean fragment) {
        if (authorizationRequest != null && authorizationRequest.getRedirectUri() != null) {
            Map<String, String> query = new LinkedHashMap();
            query.put("error", failure.getOAuth2ErrorCode());
            query.put("error_description", failure.getMessage());
            if (authorizationRequest.getState() != null) {
                query.put("state", authorizationRequest.getState());
            }

            if (failure.getAdditionalInformation() != null) {
                Iterator var5 = failure.getAdditionalInformation().entrySet().iterator();

                while(var5.hasNext()) {
                    Entry<String, String> additionalInfo = (Entry)var5.next();
                    query.put(additionalInfo.getKey(), additionalInfo.getValue());
                }
            }

            return this.append(authorizationRequest.getRedirectUri(), query, fragment);
        } else {
            throw new UnapprovedClientAuthenticationException("Authorization failure, and no redirect URI.", failure);
        }
    }

    private String append(String base, Map<String, ?> query, boolean fragment) {
        return this.append(base, query, (Map)null, fragment);
    }

    private String append(String base, Map<String, ?> query, Map<String, String> keys, boolean fragment) {
        UriComponentsBuilder template = UriComponentsBuilder.newInstance();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(base);

        URI redirectUri;
        try {
            redirectUri = builder.build(true).toUri();
        } catch (Exception var12) {
            redirectUri = builder.build().toUri();
            builder = UriComponentsBuilder.fromUri(redirectUri);
        }

        template.scheme(redirectUri.getScheme()).port(redirectUri.getPort()).host(redirectUri.getHost()).userInfo(redirectUri.getUserInfo()).path(redirectUri.getPath());
        String key;
        if (fragment) {
            StringBuilder values = new StringBuilder();
            if (redirectUri.getFragment() != null) {
                key = redirectUri.getFragment();
                values.append(key);
            }

            String name;
            for(Iterator var15 = query.keySet().iterator(); var15.hasNext(); values.append(name + "={" + key + "}")) {
                key = (String)var15.next();
                if (values.length() > 0) {
                    values.append("&");
                }

                name = key;
                if (keys != null && keys.containsKey(key)) {
                    name = (String)keys.get(key);
                }
            }

            if (values.length() > 0) {
                template.fragment(values.toString());
            }

            UriComponents encoded = template.build().expand(query).encode();
            builder.fragment(encoded.getFragment());
        } else {
            for(Iterator var13 = query.keySet().iterator(); var13.hasNext(); template.queryParam(key, new Object[]{"{" + key + "}"})) {
                key = (String)var13.next();
                key = key;
                if (keys != null && keys.containsKey(key)) {
                    key = (String)keys.get(key);
                }
            }

            template.fragment(redirectUri.getFragment());
            UriComponents encoded = template.build().expand(query).encode();
            builder.query(encoded.getQuery());
        }

        return builder.build().toUriString();
    }

    public void setUserApprovalPage(String userApprovalPage) {
        this.userApprovalPage = userApprovalPage;
    }

    public void setAuthorizationCodeServices(AuthorizationCodeServices authorizationCodeServices) {
        this.authorizationCodeServices = authorizationCodeServices;
    }

    public void setRedirectResolver(RedirectResolver redirectResolver) {
        this.redirectResolver = redirectResolver;
    }

    public void setUserApprovalHandler(UserApprovalHandler userApprovalHandler) {
        this.userApprovalHandler = userApprovalHandler;
    }

    public void setOAuth2RequestValidator(OAuth2RequestValidator oauth2RequestValidator) {
        this.oauth2RequestValidator = oauth2RequestValidator;
    }

    public void setImplicitGrantService(ImplicitGrantService implicitGrantService) {
    }

    @ExceptionHandler({ClientRegistrationException.class})
    public ModelAndView handleClientRegistrationException(Exception e, ServletWebRequest webRequest) throws Exception {
        this.logger.info("Handling ClientRegistrationException error: " + e.getMessage());
        return this.handleException(new BadClientCredentialsException(), webRequest);
    }

    @ExceptionHandler({OAuth2Exception.class})
    public ModelAndView handleOAuth2Exception(OAuth2Exception e, ServletWebRequest webRequest) throws Exception {
        this.logger.info("Handling OAuth2 error: " + e.getSummary());
        return this.handleException(e, webRequest);
    }

    @ExceptionHandler({HttpSessionRequiredException.class})
    public ModelAndView handleHttpSessionRequiredException(HttpSessionRequiredException e, ServletWebRequest webRequest) throws Exception {
        this.logger.info("Handling Session required error: " + e.getMessage());
        return this.handleException(new AccessDeniedException("Could not obtain authorization request from session", e), webRequest);
    }

    private ModelAndView handleException(Exception e, ServletWebRequest webRequest) throws Exception {
        ResponseEntity<OAuth2Exception> translate = this.getExceptionTranslator().translate(e);
        webRequest.getResponse().setStatus(translate.getStatusCode().value());
        if (!(e instanceof ClientAuthenticationException) && !(e instanceof RedirectMismatchException)) {
            AuthorizationRequest authorizationRequest = null;

            try {
                authorizationRequest = this.getAuthorizationRequestForError(webRequest);
                String requestedRedirectParam = (String)authorizationRequest.getRequestParameters().get("redirect_uri");
                String requestedRedirect = this.redirectResolver.resolveRedirect(requestedRedirectParam, this.getClientDetailsService().loadClientByClientId(authorizationRequest.getClientId()));
                authorizationRequest.setRedirectUri(requestedRedirect);
                String redirect = this.getUnsuccessfulRedirect(authorizationRequest, (OAuth2Exception)translate.getBody(), authorizationRequest.getResponseTypes().contains("token"));
                return new ModelAndView(new RedirectView(redirect, false, true, false));
            } catch (OAuth2Exception var8) {
                return new ModelAndView(this.errorPage, Collections.singletonMap("error", translate.getBody()));
            }
        } else {
            return new ModelAndView(this.errorPage, Collections.singletonMap("error", translate.getBody()));
        }
    }

    private AuthorizationRequest getAuthorizationRequestForError(ServletWebRequest webRequest) {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest)this.sessionAttributeStore.retrieveAttribute(webRequest, "authorizationRequest");
        if (authorizationRequest != null) {
            return authorizationRequest;
        } else {
            Map<String, String> parameters = new HashMap();
            Map<String, String[]> map = webRequest.getParameterMap();
            Iterator var5 = map.keySet().iterator();

            while(var5.hasNext()) {
                String key = (String)var5.next();
                String[] values = (String[])map.get(key);
                if (values != null && values.length > 0) {
                    parameters.put(key, values[0]);
                }
            }

            try {
                return this.getOAuth2RequestFactory().createAuthorizationRequest(parameters);
            } catch (Exception var8) {
                return this.getDefaultOAuth2RequestFactory().createAuthorizationRequest(parameters);
            }
        }
    }
}
