package com.devcreativa.customers.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * {@summary UserRequest dtos.}
 *
 * @author wualtervera
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private String id;
  private String name;
  private String username;
  private List<String> roles;

  private Date createdAt;
  private Date updatedAt;
}
