package com.devcreativa.customers.services;

import com.devcreativa.customers.models.entities.Customer;
import com.devcreativa.customers.models.request.CustomerRequest;
import com.devcreativa.customers.models.response.CustomerResponse;
import com.devcreativa.customers.repositories.CustomerRepository;
import com.devcreativa.customers.services.impl.CustomerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(SpringExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CustomerServiceImpl service;

    private CustomerRequest customerRequest = null;
    private CustomerResponse customerResponse = null;
    private Customer customer = null;

    @BeforeEach
    void setUp() {
        customer = new Customer("61c147ef9a746217024a9e52", "Bill", "VG", "Trujillo 1", new Date(), new Date());
        customerRequest = new CustomerRequest("61c147ef9a746217024a9e52", "Bill", "VG", "Trujillo 1");
        customerResponse = new CustomerResponse("61c147ef9a746217024a9e52", "Bill", "VG", "Trujillo 1");
    }

    @Test
    void findAll() {
        List<Customer> list = new ArrayList<>();
        list.add(customer);

        when(this.repository.findAll()).thenReturn(list);
        when(modelMapper.map(any(), any())).thenReturn(customerResponse);

        int size = this.service.findAll().size();

        assertEquals(1, size);
        verify(repository, times(1)).findAll();
    }

    @Test
    void findById() {
        when(this.repository.findById(anyString())).thenReturn(Optional.ofNullable(customer));
        when(modelMapper.map(any(), any())).thenReturn(customerResponse);

        CustomerResponse customerResponse = service.findById(customerRequest.getId());

        assertEquals(customerRequest.getId(), customerResponse.getId());
        verify(repository, times(1)).findById(customer.getId());
    }
    @Test
    void findByIdNotFound() {
        when(this.repository.findById(anyString())).thenReturn(Optional.empty());

        CustomerResponse customerResponse = service.findById(customer.getId());

        assertNull(customerResponse);
    }

    @Test
    void save() {

        when(modelMapper.map(any(CustomerRequest.class), any())).thenReturn(customer);
        when(this.repository.save(any(Customer.class))).thenReturn(customer);
        when(modelMapper.map(any(Customer.class), any())).thenReturn(customerResponse);
        CustomerResponse customerResponse = this.service.save(customerRequest);

        assertNotNull(customerResponse);
    }

    @Test
    void update() {
        when(modelMapper.map(any(), any())).thenReturn(customerResponse);
        when(this.repository.findById(anyString())).thenReturn(Optional.of(customer));

        this.customer.setName("Will 3");
        when(this.repository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse customerResponses = this.service.update(customerRequest, customerRequest.getId());

        assertEquals(customer.getName(), customerResponses.getName());
    }

    @Test
    void updateCustomerNotFound() {

        when(this.repository.findById(anyString()))
            .thenReturn(Optional.empty());

        CustomerResponse customerResponses = this.service.update(customerRequest, customerRequest.getId());
        assertNull(customerResponses);
    }


    @Test
    void updatePartial() {
        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Will 2");

        when(this.repository.findById(anyString())).thenReturn(Optional.ofNullable(customer));

        this.customerResponse.setName("Will 2");
        when(modelMapper.map(any(), any())).thenReturn(customerResponse);

        when(this.repository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse customerResp = this.service.updatePartial(customerRequest.getId(), fields);

        assertEquals("Will 2", customerResp.getName());
    }

    @Test
    void updatePartialCustomerNotFound() {
        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Will 2");

        when(this.repository.findById(anyString()))
            .thenReturn(Optional.empty());

        CustomerResponse customerResponses = this.service.updatePartial(customerRequest.getId(), fields);
        assertNull(customerResponses);
    }

    @Test
    void delete() {

        when(this.repository.existsById(anyString()))
            .thenReturn(true);

        doNothing().when(this.repository).deleteById(customerRequest.getId());

        boolean delete = service.delete(customerRequest.getId());

        assertTrue(delete);

    }

    @Test
    void deleteCustomerNotFound() {

        when(this.repository.existsById(anyString()))
            .thenReturn(false);

        boolean delete = service.delete(customerRequest.getId());

        assertFalse(delete);
    }

    @Test
    void existId() {

        when(this.repository.existsById(anyString())).thenReturn(true);

        boolean exist = this.service.existsById(customerRequest.getId());

        assertTrue(exist);
    }

}