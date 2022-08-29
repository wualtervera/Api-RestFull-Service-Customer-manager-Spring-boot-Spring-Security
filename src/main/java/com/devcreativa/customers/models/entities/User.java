package com.devcreativa.customers.models.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * {@summary User dtos.}
 *
 * @author wualtervera
 */

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("users")
public class User implements Serializable {
  @Id
  private String id;
  private String name;
  private String username;
  private String password;
  private List<String> roles;
  private Date createdAt;
  private Date updatedAt;
}
