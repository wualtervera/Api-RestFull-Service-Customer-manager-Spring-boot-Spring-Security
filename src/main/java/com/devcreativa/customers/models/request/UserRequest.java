package com.devcreativa.customers.models.request;

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
public class UserRequest {
  private String id;
  private String name;
  private String username;
  private String password;
  private List<String> roles;
}
