package com.devcreativa.customers.mocks;

import com.devcreativa.customers.models.request.CustomerRequest;
import com.devcreativa.customers.models.entities.Customer;

public class MockCustomers {
    public static CustomerRequest toCustomer(Customer customer) {
        CustomerRequest c = new CustomerRequest();
        c.setId(customer.getId());
        c.setName(customer.getName());
        c.setSurname(customer.getSurname());
        c.setDirection(customer.getDirection());
        return c;
    }

    public static Customer toCustomerDao(CustomerRequest customerRequest) {
        Customer cd = new Customer();
        cd.setId(customerRequest.getId());
        cd.setName(customerRequest.getName());
        cd.setSurname(customerRequest.getSurname());
        cd.setDirection(customerRequest.getDirection());
        return cd;
    }
}
