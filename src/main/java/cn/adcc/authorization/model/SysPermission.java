package cn.adcc.authorization.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
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
}
