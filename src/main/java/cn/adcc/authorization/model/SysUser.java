package cn.adcc.authorization.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@ApiModel(value = "SysUser", description = "用户实体")
public class SysUser extends BaseModel {
    private String username;
    private String password;
    private String nickname;
    private String headImgUrl;
    private String phone;
    private String email;
    private Date birthday;
    private Integer sex;
    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired;
    private Boolean accountNonLocked;
    private String description;
}
