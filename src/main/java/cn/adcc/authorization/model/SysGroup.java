package cn.adcc.authorization.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "groups")
@ApiModel(value = "SysGroup", description = "角色实体")
public class SysGroup extends BaseModel {
    private String name;
    private String description;
    /*@ManyToMany(mappedBy = "groups")
    private List<SysUser> users = new ArrayList<>();*/
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "group_permission",
            joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private List<SysPermission> permissions = new ArrayList<>();
}
