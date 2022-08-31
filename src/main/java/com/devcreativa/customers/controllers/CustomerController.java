package com.devcreativa.customers.controllers;

import com.devcreativa.customers.models.request.CustomerRequest;
import com.devcreativa.customers.models.response.CustomerResponse;
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
public class CustomerController implements BaseController<CustomerRequest, CustomerResponse> {

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<CustomerResponse>> getAll() {

        List<CustomerResponse> customerResponses = this.customerService.findAll();

        if (customerResponses.isEmpty())
            return responseEntity(responseError(Constants.RESOURSE_NOT_FOUND), HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(customerResponses);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CustomerResponse> getOne(String id) throws Exception {

        CustomerResponse customersResponse = this.customerService.findById(id);

        if (null == customersResponse)
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.OK).body(customersResponse);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CustomerResponse> save(CustomerRequest customerRequest) {

        CustomerResponse customerResponse  = this.customerService.save(customerRequest);

        if (null == customerResponse)
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(customerResponse);
    }

    @Operation(summary = "Update all properties") //swagger
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CustomerResponse> update(String id, CustomerRequest customerRequest) {
        CustomerResponse customerResponse = this.customerService.update(customerRequest, id);

        if (null == customerResponse)
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(customerResponse);
    }

    @Operation(summary = "Update partial properties(name, surname, etc)") //swagger
    @PatchMapping(value = "/{id}")
    public ResponseEntity<CustomerResponse> updatePartial(@PathVariable String id, @RequestBody Map<Object, Object> fields) {

        if (!customerService.existsById(id))
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        CustomerResponse customerResponse = this.customerService.updatePartial(id, fields);
        return ResponseEntity.ok(customerResponse);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CustomerResponse> delete(String id) {
        boolean deleted = this.customerService.delete(id);

        if (!deleted)
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        return ResponseEntity.noContent().build();
    }
}
