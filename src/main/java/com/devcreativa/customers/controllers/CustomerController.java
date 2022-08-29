package com.devcreativa.customers.controllers;

import com.devcreativa.customers.models.dtos.CustomerDTO;
import com.devcreativa.customers.models.dtos.UserDTO;
import com.devcreativa.customers.services.CustomerService;
import com.devcreativa.customers.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.devcreativa.customers.utils.Utils.responseEntity;
import static com.devcreativa.customers.utils.Utils.responseError;

/**
 * {@summary Controller api.}
 *
 * @author wualtervera
 */

@SecurityRequirement(name = "carlos")
@RestController
@RequestMapping(path = "api/v1/customer")
public class CustomerController implements BaseController<CustomerDTO> {

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<CustomerDTO>> getAll() {

        List<CustomerDTO> customerDTOS = this.customerService.findAll();

        if (customerDTOS.isEmpty())
            return responseEntity(responseError(Constants.RESOURSE_NOT_FOUND), HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(customerService.findAll());
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CustomerDTO> getOne(String id) throws Exception {
        CustomerDTO customers = this.customerService.findById(id);

        if (null == customers)
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CustomerDTO> save(CustomerDTO customerDTO) {

        CustomerDTO customerDTODB = this.customerService.save(customerDTO);

        if (null == customerDTODB)
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(customerDTODB);
    }

    @Operation(summary = "Update all properties") //swagger
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CustomerDTO> update(String id, CustomerDTO customerDTO) {
        CustomerDTO customerDTODB = this.customerService.update(customerDTO, id);

        if (null == customerDTODB)
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(customerDTODB);
    }

    @Operation(summary = "Update partial properties(name, surname, etc)") //swagger
    @PatchMapping(value = "/{id}")
    public ResponseEntity<CustomerDTO> updatePartial(@PathVariable String id, @RequestBody Map<Object, Object> fields) {

        if (!customerService.existsById(id))
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        CustomerDTO customerDTODB = this.customerService.updatePartial(id, fields);
        return ResponseEntity.ok(customerDTODB);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CustomerDTO> delete(String id) {
        boolean deleted = this.customerService.delete(id);

        if (!deleted)
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        return ResponseEntity.noContent().build();
    }
}
