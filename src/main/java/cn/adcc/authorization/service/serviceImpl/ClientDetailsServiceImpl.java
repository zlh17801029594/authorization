package cn.adcc.authorization.service.serviceImpl;

import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@Transactional
public class ClientDetailsServiceImpl implements ClientDetailsService {
    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        return null;
    }
}
