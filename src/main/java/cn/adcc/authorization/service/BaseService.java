package cn.adcc.authorization.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface BaseService<T> {
    T findById(Long id);

    List<T> findAll();

    Page<T> findAll(PageRequest pageRequest);

    T save(T t);

    void delete(Long id);

    void delete(List<Long> ids);
}
