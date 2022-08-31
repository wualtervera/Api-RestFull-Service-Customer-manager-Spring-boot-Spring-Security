package com.devcreativa.customers.security;

import lombok.*;

import java.util.List;

/**
 * {@summary UserRequest dtos.}
 *
 * @author wualtervera
 */


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserAuth {
  private String username;
  private String password;
  private List<String> roles;
}
