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
@Table(name = "permission")
@ApiModel(value = "SysPermission", description = "权限实体")
public class SysPermission extends BaseModel {
    private Integer parentId;
    private String name;
    private String icon;
    private String servicePrefix;
    private String uri;
    private String method;
    private Integer type;
    private Integer sort;
    /*@ManyToMany(mappedBy = "permissions")
    private List<SysGroup> groups = new ArrayList<>();*/
}
