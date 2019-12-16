package cn.adcc.authorization.dao;

import cn.adcc.authorization.model.SysClientDetails;

public interface SysClientReposity extends BaseRepository<SysClientDetails> {
    SysClientDetails findByClientId(String clientId);
}
