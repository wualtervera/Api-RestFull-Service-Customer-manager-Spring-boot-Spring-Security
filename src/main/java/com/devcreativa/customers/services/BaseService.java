package com.devcreativa.customers.services;

import java.util.List;
import java.util.Map;

/**
 * {@summary Base service.}
 *
 * @author wualtervera
 */

public interface BaseService<E, R> {
  List<R> findAll();

  R findById(String id) throws Exception;

  R save(E e);

  R update(E e, String id);

  boolean delete(String id);

  boolean existsById(String id);

  R updatePartial(String id, Map<Object, Object> fields);
}
