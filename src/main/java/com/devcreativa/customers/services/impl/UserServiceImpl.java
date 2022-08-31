package com.devcreativa.customers.services.impl;

import com.devcreativa.customers.models.request.UserRequest;
import com.devcreativa.customers.models.response.UserResponse;
import com.devcreativa.customers.models.entities.User;
import com.devcreativa.customers.repositories.UserRepository;
import com.devcreativa.customers.security.Roles;
import com.devcreativa.customers.security.UserAuth;
import com.devcreativa.customers.security.UserDetailImpl;
import com.devcreativa.customers.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * {@summary UserRequest service api.}
 *
 * @author wualtervera
 */

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private UserRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserAuth> user = this.repository.findByUsername(username).map(this::toUserAth);

        /*if (user.isPresent())
            log.info("UserRequest-login -> {}", user.get());*/

        return user.map(UserDetailImpl::new)
            .orElseThrow(() -> new UsernameNotFoundException(username + "Not found"));
    }

    @Override
    public List<UserResponse> findAll() {
        return this.repository.findAll()
            .stream().map(this::userToUserResponse)
            .collect(Collectors.toList());
    }

    @Override
    public UserResponse findById(String id) {
        Optional<UserResponse> user = this.repository.findById(id)
            .map(this::userToUserResponse);

        return user.orElse(null);
    }

    @Override
    public UserResponse save(UserRequest userRequest) {

        User user = this.userRequestToUser(userRequest);

        user.setRoles(Roles.getUserAccess());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        User userRes = this.repository.save(user);

        LOGGER.info("UserService-save -> {}", userRes);
        return this.userToUserResponse(userRes);
    }

    @Override
    public UserResponse update(UserRequest userRequest, String id) {

        Optional<User> userDB = repository.findById(id);

        if (userDB.isEmpty())
            return null;

        User user = userDB.get();

        user.setId(id);
        user.setName(userRequest.getName());
        user.setName(userRequest.getUsername());
        user.setUpdatedAt(new Date());

        User userRes = this.repository.save(user);

        LOGGER.info("UserService-update -> {}", userRes);
        return this.userToUserResponse(userRes);
    }

    @Override
    public UserResponse updatePartial(String id, Map<Object, Object> fields) {

        Optional<User> userDB = this.repository.findById(id);

        if (userDB.isEmpty())
            return null;

        User user = userDB.get();

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(User.class, (String) key);
            field.setAccessible(true);
            if (key.equals("password"))
                value = new BCryptPasswordEncoder().encode(value.toString());
            if (key.equals("updatedAt"))
                value = new Date();
            ReflectionUtils.setField(field, user, value);
        });

        User userResp = this.repository.save(user);
        LOGGER.info("UserService-updatePartial -> {}", userResp);

        return this.userToUserResponse(userResp);
    }

    @Override
    public boolean delete(String id) {
        if (!this.existsById(id)) {
            return false;
        }
        this.repository.deleteById(id);
        LOGGER.info("UserService-deleted -> {}", id);
        return true;
    }

    @Override
    public boolean existsById(String id) {
        return this.repository.existsById(id);
    }

    //add access

    @Override
    public UserResponse saveAdmin(UserRequest userRequest) {

        Optional<User> userDB = this.repository.findByRoles(Roles.getAdminAccess());

        if (userDB.isPresent())
            return null;

        User user = new User();

        user.setRoles(Roles.getAdminAccess());
        user.setPassword(new BCryptPasswordEncoder().encode(userRequest.getPassword()));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        User userResp = this.repository.save(user);
        LOGGER.info("UserAdmin-save -> {}", userResp);
        return this.userToUserResponse(userResp);
    }

    @Override
    public UserResponse accessUser(String id) {

        Optional<User> userDB = this.repository.findById(id);

        if (userDB.isEmpty())
            return null;

        User user = userDB.get();
        user.setRoles(Roles.getUserAccess());
        user.setUpdatedAt(new Date());

        User userResp = this.repository.save(user);
        LOGGER.info("UserService-accessUser -> {}", userResp);

        return this.userToUserResponse(userResp);
    }

    @Override
    public UserResponse accessModerator(String id) {

        Optional<User> userDB = this.repository.findById(id);

        if (userDB.isEmpty())
            return null;

        /*User userDB = this.repository.findByIdAndRoles(id, userRequest.getRoles()).orElse(null);

        if (null == userDB)
            return null;*/

        User user = userDB.get();

        user.setRoles(Roles.getModeratorAccess());
        user.setUpdatedAt(new Date());

        User userResp = this.repository.save(user);
        LOGGER.info("UserService-accessModerator -> {}", userResp);

        return this.userToUserResponse(user);
    }

    //mappers
    private UserResponse userToUserResponse(User user){
        return this.modelMapper.map(user, UserResponse.class);
    }
    private User userRequestToUser(UserRequest userRequestser){
        return this.modelMapper.map(userRequestser, User.class);
    }


    /**
     * {@summary Mapper entity to dto.}
     *
     * @param user entity
     * @return UserAuth dto
     */
    public UserAuth toUserAth(User user) {
        UserAuth userAuth = new UserAuth();
        userAuth.setUsername(user.getUsername());
        userAuth.setPassword(user.getPassword());
        userAuth.setRoles(user.getRoles());
        return userAuth;
    }
}
