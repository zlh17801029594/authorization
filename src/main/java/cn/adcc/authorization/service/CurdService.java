package cn.adcc.authorization.service;

import java.util.List;

public interface CurdService<T> {
    void save(T record);

    void delete(Long id);

    void delete(List<Long> ids);

    void update(T record);

    T findById(Long id);

    List<T> findAll();
}
