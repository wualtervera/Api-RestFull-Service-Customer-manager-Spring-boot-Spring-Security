package com.devcreativa.customers.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * {@summary UserDTO dtos.}
 *
 * @author wualtervera
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
  private String id;
  private String name;
  private String username;
  private String password;
  private List<String> roles;

  private Date createdAt;
  private Date updatedAt;
}
