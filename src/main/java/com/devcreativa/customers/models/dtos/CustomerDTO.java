package com.devcreativa.customers.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * {@summary Cutomer dtos.}
 *
 * @author wualtervera
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {
  private String id;
  private String name;
  private String surname;
  private String direction;
  private Date createdAt;
  private Date updatedAt;
}
