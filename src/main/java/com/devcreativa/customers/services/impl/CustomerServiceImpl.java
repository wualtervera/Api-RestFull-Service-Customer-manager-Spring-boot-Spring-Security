package com.devcreativa.customers.services.impl;

import com.devcreativa.customers.models.dtos.CustomerDTO;
import com.devcreativa.customers.models.entities.Customer;
import com.devcreativa.customers.repositories.CustomerRepository;
import com.devcreativa.customers.services.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @Override
    public List<CustomerDTO> findAll() {
        return repository.findAll()
            .stream().map(this::toCustomerDTO)
            .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO findById(String id) {
        return repository.findById(id)
            .map(this::toCustomerDTO)
            .orElse(null);
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) {
        customerDTO.setCreatedAt(new Date());
        customerDTO.setUpdatedAt(new Date());
        Customer customer = repository.save(this.toCustomer(customerDTO));
        LOGGER.info("UserDTO-save -> {}", customer);
        return this.toCustomerDTO(customer);
    }

    @Override
    public CustomerDTO update(CustomerDTO customerDTO, String id) {

        CustomerDTO customerDTODB = this.findById(id);
        if (null == customerDTODB)
            return null;

        customerDTODB.setId(id);
        customerDTODB.setName(customerDTO.getName());
        customerDTODB.setSurname(customerDTO.getSurname());
        customerDTODB.setDirection(customerDTO.getDirection());
        customerDTODB.setCreatedAt(customerDTODB.getCreatedAt());
        customerDTODB.setUpdatedAt(new Date());

        Customer customer = repository.save(this.toCustomer(customerDTODB));
        LOGGER.info("UserDTO-update -> {}", customer);
        return this.toCustomerDTO(customer);
    }

    @Override
    public CustomerDTO updatePartial(String id, Map<Object, Object> fields) {

        CustomerDTO customerDTODB = this.findById(id);

        if (null == customerDTODB)
            return null;

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(CustomerDTO.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, customerDTODB, value);
        });

        customerDTODB.setUpdatedAt(new Date());

        Customer customer = this.repository.save(this.toCustomer(customerDTODB));
        LOGGER.info("UserDTO-updatePartial -> {}", customer);
        return this.toCustomerDTO(customer);
    }

    @Override
    public boolean delete(String id) {
        if (!this.existsById(id)) {
            LOGGER.info("CustomerDTO not found! -> {}", id);
            return false;
        }
        this.repository.deleteById(id);
        LOGGER.info("CustomerDTO deleted -> {}", id);
        return true;
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    /**
     * {@summary Mapper entity to dto.}
     *
     * @param customer entity
     * @return CustomerDTO
     */
    public CustomerDTO toCustomerDTO(Customer customer) {
        CustomerDTO c = new CustomerDTO();
        c.setId(customer.getId());
        c.setName(customer.getName());
        c.setSurname(customer.getSurname());
        c.setDirection(customer.getDirection());
        c.setCreatedAt(customer.getCreatedAt());
        c.setUpdatedAt(customer.getUpdatedAt());
        return c;
    }

    /**
     * {@summary Mapper dto to entity.}
     *
     * @param customerDTO dto
     * @return Customer entity
     */
    public Customer toCustomer(CustomerDTO customerDTO) {
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