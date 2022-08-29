package com.devcreativa.customers.services;

import com.devcreativa.customers.models.dtos.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * {@summary Base service.}
 *
 * @author wualtervera
 */

public interface BaseService<E> {
  List<E> findAll();

  E findById(String id) throws Exception;

  E save(E e);

  E update(E e, String id);

  boolean delete(String id);

  boolean existsById(String id);

  E updatePartial(String id, Map<Object, Object> fields);
}
