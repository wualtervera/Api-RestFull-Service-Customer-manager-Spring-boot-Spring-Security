package com.devcreativa.customers.mocks;

import com.devcreativa.customers.models.entities.Customer;
import com.devcreativa.customers.models.dtos.CustomerDTO;

public class MockCustomers {
    public static CustomerDTO toCustomer(Customer customer) {
        CustomerDTO c = new CustomerDTO();
        c.setId(customer.getId());
        c.setName(customer.getName());
        c.setSurname(customer.getSurname());
        c.setDirection(customer.getDirection());
        c.setCreatedAt(customer.getCreatedAt());
        c.setUpdatedAt(customer.getUpdatedAt());
        return c;
    }

    public static Customer toCustomerDao(CustomerDTO customerDTO) {
        Customer cd = new Customer();
        cd.setId(customerDTO.getId());
        cd.setName(customerDTO.getName());
        cd.setSurname(customerDTO.getSurname());
        cd.setDirection(customerDTO.getDirection());
        cd.setCreatedAt(customerDTO.getCreatedAt());
        cd.setUpdatedAt(customerDTO.getUpdatedAt());
        return cd;
    }
}
