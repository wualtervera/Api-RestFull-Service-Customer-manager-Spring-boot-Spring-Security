package com.devcreativa.customers.controllers;

import com.devcreativa.customers.models.request.UserRequest;
import com.devcreativa.customers.models.response.UserResponse;
import com.devcreativa.customers.services.UserService;
import com.devcreativa.customers.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@SecurityRequirement(name = "carlos")
@RestController
@RequestMapping("")
public class UserController implements BaseController<UserRequest,UserResponse> {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<UserResponse>> getAll() {

        List<UserResponse> userRequests = this.userService.findAll();
        if (userRequests.isEmpty())
            return responseEntity(responseError(Constants.RESOURSE_NOT_FOUND), HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(this.userService.findAll());
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<UserResponse> getOne(@PathVariable String id) throws Exception {

        UserResponse userRequest = this.userService.findById(id);
        if (null == userRequest)
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.OK).body(userRequest);
    }

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<UserResponse> save(@RequestBody UserRequest userRequest) {

        UserResponse userResponse = this.userService.save(userRequest);

        if (null == userResponse)
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userResponse);
    }

    @Operation(summary = "Update all properties") //swagger
    @PutMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<UserResponse> update(@PathVariable String id, @RequestBody UserRequest userRequest) {

        UserResponse userResponse = this.userService.update(userRequest, id);

        if (null == userResponse)
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Update partial properties(passwor, name, etc)") //swagger
    @PatchMapping(value = "/users/{id}")
    public ResponseEntity<UserResponse> updatePartial(@PathVariable String id, @RequestBody Map<Object, Object> fields) {

        if (!userService.existsById(id))
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        UserResponse userResponse = this.userService.updatePartial(id, fields);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<?> delete(@PathVariable String id) throws Exception {
        boolean delete = this.userService.delete(id);
        if (delete)
            return ResponseEntity.noContent().build();

        return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);
    }


    //add access
    @Operation(summary = "Grant admin permissions") //swagger
    @PostMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> addAdmin(@RequestBody UserRequest userRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.saveAdmin(userRequest));
    }

    @Operation(summary = "Grant moderator permissions") //swagger
    @PutMapping(value = "/moderator/{id_user}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> addAccessModeator(@PathVariable String idUser) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.accessModerator(idUser));
    }

    @Operation(summary = "Grant user permissions") //swagger
    @PutMapping(value = "/default/{id_user}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> addAccessUser(@PathVariable String idUser) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.accessUser(idUser));
    }


    //auth to swagger test session

    @Operation(summary = "Log out of Swagger test session")
    @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout() {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
            .body("{\"statusCode\": \"Closed authentication\"}");
    }

    @PostMapping(
        value = "/login",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Log int of Swagger test session")
    public ResponseEntity<String> login(@Parameter String username, @Parameter String password) {

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
            .body("{\"statusCode\": \"Successful authentication\"}");
    }

}