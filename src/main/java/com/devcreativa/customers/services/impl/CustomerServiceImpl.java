package com.devcreativa.customers.services.impl;

import com.devcreativa.customers.models.entities.Customer;
import com.devcreativa.customers.models.request.CustomerRequest;
import com.devcreativa.customers.models.response.CustomerResponse;
import com.devcreativa.customers.repositories.CustomerRepository;
import com.devcreativa.customers.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@summary Customer service api.}
 *
 * @author wualtervera
 */

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CustomerResponse> findAll() {
        return repository.findAll()
            .stream().map(this::customerToCustomerResponse)
            .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse findById(String id) {
        Optional<Customer> customer = repository.findById(id);
        if (customer.isEmpty())
            return null;
        return customerToCustomerResponse(customer.get());
    }

    @Override
    public CustomerResponse save(CustomerRequest customerRequest) {

        Customer customer = this.customerRequestToCustomer(customerRequest);
        customer.setCreatedAt(new Date());
        customer.setUpdatedAt(new Date());

        Customer customerResp = repository.save(customer);
        LOGGER.info("UserRequest-save -> {}", customerResp);
        return this.customerToCustomerResponse(customerResp);
    }

    @Override
    public CustomerResponse update(CustomerRequest customerRequest, String id) {

        Optional<Customer> customerDB = this.repository.findById(id);

        if (customerDB.isEmpty())
            return null;

        Customer customer = customerDB.get();

        customer.setId(id);
        customer.setName(customerRequest.getName());
        customer.setSurname(customerRequest.getSurname());
        customer.setDirection(customerRequest.getDirection());
        customer.setCreatedAt(customer.getCreatedAt());
        customer.setUpdatedAt(new Date());

        Customer customerResp = repository.save(customer);
        LOGGER.info("UserRequest-update -> {}", customerResp);
        return this.customerToCustomerResponse(customerResp);
    }

    @Override
    public CustomerResponse updatePartial(String id, Map<Object, Object> fields) {

        Optional<Customer> customerDB = this.repository.findById(id);

        if (customerDB.isEmpty())
            return null;

        Customer customerRef = customerDB.get();

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Customer.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, customerRef, value);
        });

        //Customer customer = customerDB.get();
        customerRef.setUpdatedAt(new Date());

        Customer customerResp = this.repository.save(customerRef);
        LOGGER.info("UserRequest-updatePartial -> {}", customerResp);
        return this.customerToCustomerResponse(customerResp);
    }

    @Override
    public boolean delete(String id) {
        if (!this.existsById(id)) {
            LOGGER.info("CustomerRequest not found! -> {}", id);
            return false;
        }
        this.repository.deleteById(id);
        LOGGER.info("CustomerRequest deleted -> {}", id);
        return true;
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    //mappers
    private CustomerResponse customerToCustomerResponse(Customer customer){
        return this.modelMapper.map(customer, CustomerResponse.class);
    }

    private Customer customerRequestToCustomer(CustomerRequest customerRequest){
        return this.modelMapper.map(customerRequest, Customer.class);
    }


}