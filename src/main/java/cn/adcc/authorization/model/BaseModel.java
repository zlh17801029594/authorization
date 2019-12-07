package cn.adcc.authorization.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BaseModel {
    private Long id;
    private Boolean enable;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String createBy;
    private String updateBy;
}
