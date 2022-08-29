package com.devcreativa.customers.controllers;

import com.devcreativa.customers.models.dtos.CustomerDTO;
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

    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        this.customerDTO = new CustomerDTO("61c147ef9a746217024a9e5a", "Will", "V", "Trujillo", new Date(), new Date());
    }

    @Test
    void getAll() {
      List<CustomerDTO> list = new ArrayList<>();

      list.add(this.customerDTO);
      list.add(this.customerDTO);

      when(this.service.findAll()).thenReturn(list);
      ResponseEntity<List<CustomerDTO>> res = this.controller.getAll();

      assertEquals(HttpStatus.OK, res.getStatusCode());

      assertEquals(2, res.getBody().size());
    }

    @Test
    void getAllListEmpty() {
        List<CustomerDTO> list = new ArrayList<>();

        when(this.service.findAll()).thenReturn(list);
        ResponseEntity<List<CustomerDTO>> res = this.controller.getAll();

        assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
    }

    @Test
    void getOne() throws Exception {

        when(this.service.findById(this.customerDTO.getId())).thenReturn(this.customerDTO);

        ResponseEntity<CustomerDTO> res = this.controller.getOne(this.customerDTO.getId());

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(this.customerDTO, res.getBody());

        ResponseEntity<CustomerDTO> res2 = this.controller.getOne("5"); //NO EXIST ID
        System.out.println("res = " + res2); //OK
        assertEquals(HttpStatus.NOT_FOUND, res2.getStatusCode());

        verify(service, times(1)).findById(this.customerDTO.getId());
    }
    @Test
    void getOneIdNotFound() throws Exception {

        when(this.service.findById(this.customerDTO.getId())).thenReturn(null);

        ResponseEntity<CustomerDTO> res = this.controller.getOne(this.customerDTO.getId());

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    void save() {
        when(this.service.save(this.customerDTO)).thenReturn(this.customerDTO);

        ResponseEntity<CustomerDTO> res = this.controller.save(this.customerDTO);

        assertEquals(HttpStatus.CREATED, res.getStatusCode());

        assertEquals(this.customerDTO, res.getBody());
    }
    @Test
    void saveResponseNull() {
        when(this.service.save(this.customerDTO)).thenReturn(null);

        ResponseEntity<CustomerDTO> res = this.controller.save(this.customerDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, res.getStatusCode());
    }

    @Test
    void update() {

        this.customerDTO.setName("Will 3");

        when(this.service.existsById(this.customerDTO.getId()))
            .thenReturn(true);
        when(this.service.update(this.customerDTO, this.customerDTO.getId()))
            .thenReturn(this.customerDTO);

        ResponseEntity<CustomerDTO> res = this.controller.update(this.customerDTO.getId(), this.customerDTO);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(this.customerDTO.getName(), res.getBody().getName());
    }

    @Test
    void updateIdNotFound() {

        when(this.service.update(this.customerDTO, this.customerDTO.getId()))
            .thenReturn(null);

        ResponseEntity<CustomerDTO> res = this.controller.update(this.customerDTO.getId(), this.customerDTO);

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }



    @Test
    void updatePartial() {

        this.customerDTO.setName("Will 3");

        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Will 2");


        when(this.service.existsById(this.customerDTO.getId()))
            .thenReturn(true);
        when(this.service.updatePartial(this.customerDTO.getId(), fields))
            .thenReturn(this.customerDTO);

        ResponseEntity<CustomerDTO> res = this.controller.updatePartial(this.customerDTO.getId(), fields);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(this.customerDTO.getName(), res.getBody().getName());
    }

    @Test
    void updatePartialIdNotFound() {
        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Will 2");

        when(this.service.update(this.customerDTO, this.customerDTO.getId()))
            .thenReturn(null);

        ResponseEntity<CustomerDTO> res = this.controller.updatePartial(this.customerDTO.getId(), fields);

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    void delete() {
        when(this.service.delete(this.customerDTO.getId())).thenReturn(true);

        ResponseEntity<?> res = this.controller.delete(this.customerDTO.getId());

        assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
    }
    @Test
    void deleteIdNotFound() {

        when(this.service.delete(this.customerDTO.getId())).thenReturn(false);

        ResponseEntity<?> res = this.controller.delete(this.customerDTO.getId());

        assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());

        verify(service, times(1)).delete(this.customerDTO.getId());
    }
}