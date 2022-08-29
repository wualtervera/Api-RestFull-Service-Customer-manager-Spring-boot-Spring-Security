package com.devcreativa.customers.repositories;

import com.devcreativa.customers.models.entities.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * {@summary Repository api.}
 *
 * @author wualtervera
 */

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
}
