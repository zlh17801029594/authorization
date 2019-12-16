package cn.adcc.authorization.dao;

import cn.adcc.authorization.model.SysClientDetails;

public interface SysClientDetailsRepository extends BaseRepository<SysClientDetails> {
    SysClientDetails findByClientId(String clientId);
}
