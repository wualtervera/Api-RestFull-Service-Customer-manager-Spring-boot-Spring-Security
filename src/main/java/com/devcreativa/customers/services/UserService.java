package com.devcreativa.customers.services;

import com.devcreativa.customers.models.dtos.UserDTO;

import java.util.Map;

public interface UserService extends BaseService<UserDTO>{

    UserDTO saveAdmin(UserDTO userDTO);
    UserDTO accessUser(String id);
    UserDTO accessModerator(String id);
}
