package cn.adcc.authorization.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "SysRole", description = "角色实体")
public class SysRole extends BaseModel {
    private String name;
    private String description;
}
