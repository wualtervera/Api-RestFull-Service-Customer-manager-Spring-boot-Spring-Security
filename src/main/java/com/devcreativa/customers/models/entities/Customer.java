package com.devcreativa.customers.models.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
/**
 * {@summary Customer to repository.}
 *
 * @author wualtervera
 */

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("customers")
public class Customer implements Serializable {
  @Id
  private String id;
  private String name;
  private String surname;
  private String direction;
  private Date createdAt;
  private Date updatedAt;
}
