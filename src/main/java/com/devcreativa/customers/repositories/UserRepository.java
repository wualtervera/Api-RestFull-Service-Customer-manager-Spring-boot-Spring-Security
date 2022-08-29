package com.devcreativa.customers.repositories;

import java.util.List;
import java.util.Optional;

import com.devcreativa.customers.models.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByUsername(String username);
  Optional<User> findByRoles(List<String> role);
  Optional<User> findByIdAndRoles(String id, List<String> role);
}
