package cn.adcc.authorization.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SysLog {
    private Long id;
    private String username;
    private String operation;
    private String method;
    private String params;
    private Long time;
    private String ip;
    private Timestamp createTime;
}
