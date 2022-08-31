package com.devcreativa.customers.services;

import com.devcreativa.customers.models.request.UserRequest;
import com.devcreativa.customers.models.response.UserResponse;

public interface UserService extends BaseService<UserRequest,UserResponse>{

    UserResponse saveAdmin(UserRequest userRequest);
    UserResponse accessUser(String id);
    UserResponse accessModerator(String id);
}
