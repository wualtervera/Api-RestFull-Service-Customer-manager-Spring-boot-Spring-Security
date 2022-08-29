package com.devcreativa.customers.services;

import com.devcreativa.customers.models.entities.Customer;
import com.devcreativa.customers.models.dtos.CustomerDTO;
import com.devcreativa.customers.repositories.CustomerRepository;
import com.devcreativa.customers.services.impl.CustomerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static com.devcreativa.customers.mocks.MockCustomers.toCustomerDao;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(SpringExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl service;

    private CustomerDTO customerDTO = null;
    private Customer customer = null;

    @BeforeEach
    void setUp() {
        customer = new Customer("61c147ef9a746217024a9e52", "Bill", "VG", "Trujillo 1", new Date(), new Date());
        customerDTO = new CustomerDTO("61c147ef9a746217024a9e52", "Bill", "VG", "Trujillo 1", new Date(), new Date());

    }

    @Test
    @Order(1)
    void findAll() {
        List<Customer> list = new ArrayList<>();

        list.add(customer);

        when(this.repository.findAll()).thenReturn(list);

        int size = this.service.findAll().size();

        assertEquals(1, size);

        verify(repository, times(1)).findAll();
    }

    @Test
    void findById() {

        when(this.repository.findById(customer.getId())).thenReturn(Optional.of(customer));

        CustomerDTO customerDTO = service.findById(customer.getId());
        System.out.println("res = " + customerDTO.getId()); //respuesta

        assertEquals(customer.getId(), customerDTO.getId());

        verify(repository, times(1)).findById(customer.getId());
    }

    @Test
    void save() {

        when(this.repository.save(any(Customer.class))).thenReturn(toCustomerDao(customerDTO));

        CustomerDTO customerDTOResp = this.service.save(customerDTO);

        assertNotNull(customerDTOResp);
    }

    @Test
    void update() {

        when(this.repository.findById(anyString()))
            .thenReturn(Optional.of(toCustomerDao(customerDTO)));

        this.customerDTO.setName("Will 3");
        when(this.repository.save(any(Customer.class)))
            .thenReturn(toCustomerDao(customerDTO));

        CustomerDTO customerDTORes = this.service.update(customerDTO, customerDTO.getId());

        assertEquals(customerDTORes.getName(), "Will 3");
    }

    @Test
    void updateCustomerNotFound() {

        when(this.repository.findById(anyString()))
            .thenReturn(Optional.empty());

        CustomerDTO customerDTORes = this.service.update(customerDTO, customerDTO.getId());
        assertNull(customerDTORes);
    }


    @Test
    void updatePartial() {
        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Will 2");

        when(this.repository.findById(anyString()))
            .thenReturn(Optional.of(toCustomerDao(customerDTO)));

        this.customerDTO.setName("Will 3");
        when(this.repository.save(any(Customer.class)))
            .thenReturn(toCustomerDao(customerDTO));

        CustomerDTO customerDTORes = this.service.updatePartial(customerDTO.getId(), fields);

        assertEquals(customerDTORes.getName(), "Will 3");
    }

    @Test
    void updatePartialCustomerNotFound() {
        Map<Object, Object> fields = new HashMap<>();
        fields.put("name", "Will 2");

        when(this.repository.findById(anyString()))
            .thenReturn(Optional.empty());

        CustomerDTO customerDTORes = this.service.updatePartial(customerDTO.getId(), fields);
        assertNull(customerDTORes);
    }

    @Test
    void delete() {

        when(this.repository.existsById(anyString()))
            .thenReturn(true);

        doNothing().when(this.repository).deleteById(customerDTO.getId());

        boolean delete = service.delete(customerDTO.getId());

        assertTrue(delete);

    }

    @Test
    void deleteCustomerNotFound() {

        when(this.repository.existsById(anyString()))
            .thenReturn(false);

        boolean delete = service.delete(customerDTO.getId());

        assertFalse(delete);
    }

    @Test
    void existId() {

        when(this.repository.existsById(anyString())).thenReturn(true);

        boolean exist = this.service.existsById(customerDTO.getId());

        assertTrue(exist);
    }

}