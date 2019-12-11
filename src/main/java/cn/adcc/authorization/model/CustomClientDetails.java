package cn.adcc.authorization.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

public class CustomClientDetails extends BaseClientDetails {
    @JsonProperty("client_info")
    private String clientInfo;
}
