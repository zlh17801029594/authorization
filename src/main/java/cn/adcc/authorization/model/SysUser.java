package cn.adcc.authorization.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.*;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
@ApiModel(value = "SysUser", description = "用户实体")
public class SysUser extends BaseModel {
    private String username;
    private String password;
    private String nickname;
    private String headImgUrl;
    private String telephone;
    private String phone;
    private String email;
    private Date birthday;
    private Integer sex;
    private Boolean accountNonExpired;
    private Boolean credentialsNonExpired = true;
    private Boolean accountNonLocked;
    private String description;
    @ManyToMany(cascade = CascadeType.MERGE)
    //试试限定条件，enable == 1
    //@Where()
    @JoinTable(name = "user_group",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
    private List<SysGroup> groups = new ArrayList<>();
}
