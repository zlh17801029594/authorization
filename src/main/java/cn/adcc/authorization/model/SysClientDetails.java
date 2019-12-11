package cn.adcc.authorization.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "client_details")
@ApiModel(value = "SysClientDetails", description = "客户端实体")
public class SysClientDetails extends BaseModel {
    private String clientId;
    private String clientSecret;
    private String clientInfo;
    private String resourceIds;
    private String scope;
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    private String authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String additionalInformation;
    private String autoApprove;
}
