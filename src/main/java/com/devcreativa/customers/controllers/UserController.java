package com.devcreativa.customers.controllers;

import com.devcreativa.customers.models.dtos.UserDTO;
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
public class UserController implements BaseController<UserDTO> {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<UserDTO>> getAll() {

        List<UserDTO> userDTOS = this.userService.findAll();
        if (userDTOS.isEmpty())
            return responseEntity(responseError(Constants.RESOURSE_NOT_FOUND), HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(this.userService.findAll());
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<UserDTO> getOne(@PathVariable String id) throws Exception {

        UserDTO userDTO = this.userService.findById(id);
        if (null == userDTO)
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO userDTO) {
        UserDTO userDTODB = this.userService.save(userDTO);

        if (null == userDTODB)
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.userService.save(userDTODB));
    }

    @Operation(summary = "Update all properties") //swagger
    @PutMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<UserDTO> update(@PathVariable String id, @RequestBody UserDTO userDTO) {

        UserDTO userDTODB = this.userService.update(userDTO, id);

        if (null == userDTODB)
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(userDTODB);
    }

    @Operation(summary = "Update partial properties(passwor, name, etc)") //swagger
    @PatchMapping(value = "/users/{id}")
    public ResponseEntity<UserDTO> updatePartial(@PathVariable String id, @RequestBody Map<Object, Object> fields) {

        if (!userService.existsById(id))
            return responseEntity(responseError(Constants.RESOURSE_ID_NOT_FOUND), HttpStatus.NOT_FOUND);

        UserDTO userDTODB = this.userService.updatePartial(id, fields);
        return ResponseEntity.ok(userDTODB);
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
    public ResponseEntity<UserDTO> addAdmin(@RequestBody UserDTO userDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.saveAdmin(userDTO));
    }

    @Operation(summary = "Grant moderator permissions") //swagger
    @PutMapping(value = "/moderator/{id_user}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addAccessModeator(@PathVariable String idUser) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.accessModerator(idUser));
    }

    @Operation(summary = "Grant user permissions") //swagger
    @PutMapping(value = "/default/{id_user}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addAccessUser(@PathVariable String idUser) {

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