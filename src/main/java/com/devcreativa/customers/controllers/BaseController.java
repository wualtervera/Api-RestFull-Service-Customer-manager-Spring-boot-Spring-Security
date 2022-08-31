package com.devcreativa.customers.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * {@summary <p>Base Controller api.<p/>}
 *
 * @author wualtervera
 */
public interface BaseController<E, R> {

  ResponseEntity<List<R>> getAll();

  ResponseEntity<R> getOne(@PathVariable String id) throws Exception;

  ResponseEntity<R> save(@RequestBody E e);

  ResponseEntity<R> update(@PathVariable String id, @RequestBody E e) throws Exception;

  ResponseEntity<?> delete(@PathVariable String id) throws Exception;
}
