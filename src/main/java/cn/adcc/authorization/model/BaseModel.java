package cn.adcc.authorization.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Setter
@Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean enable;
    @CreatedDate
    @Column(updatable = false)
    private Timestamp createTime;
    @CreatedBy
    @Column(updatable = false)
    private String createBy;
    @LastModifiedDate
    private Timestamp updateTime;
    @LastModifiedBy
    private String updateBy;
}
