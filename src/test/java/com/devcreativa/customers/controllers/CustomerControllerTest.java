package com.devcreativa.customers.controllers;

import com.devcreativa.customers.models.entities.Customer;
import com.devcreativa.customers.models.request.CustomerRequest;
import com.devcreativa.customers.models.response.CustomerResponse;
import com.devcreativa.customers.services.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerServiceImpl service;

    @InjectMocks
    private CustomerController controller;

    private CustomerRequest customerRequest;
    private CustomerResponse customerResponse;
    private Customer customer;

    @BeforeEach
    void setUp() {
        this.customer = new Customer("61c147ef9a746217024a9e5a", "Will", "V", "Trujillo", new Date(), new Date());
        this.customerResponse = new CustomerResponse("61c147ef9a746217024a9e5a", "Will", "V", "Trujillo");
        this.customerRequest = new CustomerRequest("61c147ef9a746217024a9e5a", "Will", "V", "Trujillo");

    }

    @Test
    void getAll() {
      List<CustomerResponse> list = new ArrayList<>();

      list.add(this.customerResponse);
      list.add(this.customerResponse);

      when(this.service.findAll()).thenReturn(list);
      ResponseEntity<List<CustomerResponse>> res = this.controller.getAll();

      assertEquals(HttpStatus.OK, res.getStatusCode());

      assertEquals(2, res.getBody().size());
    }

    @Test
    void getAllListEmpty() {
        List<CustomerResponse> list = new ArrayList<>();

        when(this.service.findAll()).thenReturn(list);
        ResponseEntity<List<CustomerResponse>> res = this.controller.getAll();

        assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
    }

    @Test
    void getOne() throws Exception {

        when(this.service.findById(anyString())).thenReturn(this.customerResponse);

        ResponseEntity<CustomerResponse> res = this.controller.getOne(this.customerRequest.getId());

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(this.customerResponse.getId(), res.getBody().getId());

        verify(service, times(1)).findById(this.customerRequest.getId());
    }
    @Test
    void getOneIdNotFound() throws Exception {

        when(this.service.findById(anyString())).thenReturn(null);

        ResponseEntity<CustomerResponse> res = this.controller.getOne(this.customerRequest.getId());

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    void save() {
        when(this.service.save(any(CustomerRequest.class))).thenReturn(this.customerResponse);

        ResponseEntity<CustomerResponse> res = this.controller.save(this.customerRequest);

        assertEquals(HttpStatus.CREATED, res.getStatusCode());

        assertEquals(this.customerResponse, res.getBody());
    }
    @Test
    void saveResponseNull() {
        when(this.service.save(any(CustomerRequest.class))).thenReturn(null);

        ResponseEntity<CustomerResponse> res = this.controller.save(this.customerRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
    }

    @Test
    void update() {

        when(this.service.existsById(anyString())).thenReturn(true);

        this.customerResponse.setName("Will 3");
        when(this.service.update(any(CustomerRequest.class), anyString()))
            .thenReturn(this.customerResponse);

        ResponseEntity<CustomerResponse> res = this.controller
            .update(this.customerRequest.getId(), this.customerRequest);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(this.customerResponse.getName(), res.getBody().getName());
    }

    @Test
    void updateIdNotFound() {

        when(this.service.update(any(CustomerRequest.class), anyString()))
            .thenReturn(null);

        ResponseEntity<CustomerResponse> res = this.controller.update(this.customerRequest.getId(), this.customerRequest);

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }



    @Test
    void updatePartial() {
        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Will 2");

        when(this.service.existsById(anyString())).thenReturn(true);
        this.customerResponse.setName("Will 3");
        when(this.service.updatePartial(anyString(), anyMap())).thenReturn(this.customerResponse);

        ResponseEntity<CustomerResponse> res = this.controller.updatePartial(this.customerRequest.getId(), fields);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(this.customerResponse.getName(), res.getBody().getName());
    }

    @Test
    void updatePartialIdNotFound() {
        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Will 2");

        when(this.service.update(this.customerRequest, this.customerRequest.getId()))
            .thenReturn(null);

        ResponseEntity<CustomerResponse> res = this.controller.updatePartial(this.customerRequest.getId(), fields);

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    void delete() {
        when(this.service.delete(this.customerRequest.getId())).thenReturn(true);

        ResponseEntity<?> res = this.controller.delete(this.customerRequest.getId());

        assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
    }
    @Test
    void deleteIdNotFound() {

        when(this.service.delete(this.customerRequest.getId())).thenReturn(false);

        ResponseEntity<?> res = this.controller.delete(this.customerRequest.getId());

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());

        verify(service, times(1)).delete(this.customerRequest.getId());
    }
}