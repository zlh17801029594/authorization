package cn.adcc.authorization.service.serviceImpl;

import cn.adcc.authorization.dao.SysClientDetailsRepository;
import cn.adcc.authorization.model.SysClientDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Primary
public class ClientDetailsServiceImpl implements ClientDetailsService {
    @Autowired
    private SysClientDetailsRepository sysClientDetailsRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        if (clientId != null) {
            SysClientDetails sysClientDetails = sysClientDetailsRepository.findByClientId(clientId);
            if (sysClientDetails == null) {
                throw new NoSuchClientException("客户端不存在");
            }
            BaseClientDetails clientDetails = new BaseClientDetails(sysClientDetails.getClientId(),
                    sysClientDetails.getResourceIds(),
                    sysClientDetails.getScope(),
                    sysClientDetails.getAuthorizedGrantTypes(),
                    sysClientDetails.getAuthorities(),
                    sysClientDetails.getResourceIds());
            clientDetails.setClientSecret(sysClientDetails.getClientSecret());
            if (sysClientDetails.getAccessTokenValidity() != null) {
                clientDetails.setAccessTokenValiditySeconds(sysClientDetails.getAccessTokenValidity());
            }
            if (sysClientDetails.getRefreshTokenValidity() != null) {
                clientDetails.setRefreshTokenValiditySeconds(sysClientDetails.getRefreshTokenValidity());
            }
            if (sysClientDetails.getAutoApprove() != null) {
                clientDetails.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(sysClientDetails.getAutoApprove()));
            }
            return clientDetails;
        }
        throw new NoSuchClientException("客户端不存在");
    }
}
