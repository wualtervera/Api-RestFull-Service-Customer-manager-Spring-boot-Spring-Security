package com.devcreativa.customers.services.impl;

import com.devcreativa.customers.models.dtos.UserDTO;
import com.devcreativa.customers.models.entities.User;
import com.devcreativa.customers.repositories.UserRepository;
import com.devcreativa.customers.security.Roles;
import com.devcreativa.customers.security.UserAuth;
import com.devcreativa.customers.security.UserDetailImpl;
import com.devcreativa.customers.services.UserService;
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
 * {@summary UserDTO service api.}
 *
 * @author wualtervera
 */

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserAuth> user = this.repository.findByUsername(username).map(this::toUserAth);

        /*if (user.isPresent())
            log.info("UserDTO-login -> {}", user.get());*/

        return user.map(UserDetailImpl::new)
            .orElseThrow(() -> new UsernameNotFoundException(username + "Not found"));
    }

    @Override
    public List<UserDTO> findAll() {
        return this.repository.findAll()
            .stream().map(this::toUserDTO)
            .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(String id) {
        Optional<UserDTO> user = this.repository.findById(id).map(this::toUserDTO);
        return user.orElse(null);
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        userDTO.setRoles(Roles.getUserAccess());
        userDTO.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        userDTO.setCreatedAt(new Date());
        userDTO.setUpdatedAt(new Date());

        User user = this.repository.save(this.toUser(userDTO));
        LOGGER.info("UserDTO-save -> {}", user);
        return this.toUserDTO(user);
    }

    @Override
    public UserDTO update(UserDTO userDTO, String id) {

        UserDTO userDTODB = findById(id);

        if (null == userDTODB)
            return null;

        userDTODB.setId(id);
        userDTODB.setName(userDTO.getName());
        userDTODB.setUsername(userDTO.getUsername());
        userDTODB.setPassword(userDTODB.getPassword());
        userDTODB.setRoles(userDTODB.getRoles());
        userDTODB.setCreatedAt(userDTODB.getCreatedAt());
        userDTODB.setUpdatedAt(new Date());

        User user = this.repository.save(toUser(userDTODB));
        LOGGER.info("UserDTO-update -> {}", user);

        return this.toUserDTO(user);
    }

    @Override
    public UserDTO updatePartial(String id, Map<Object, Object> fields) {
        UserDTO userDTODB = findById(id);

        if (null == userDTODB)
            return null;

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(UserDTO.class, (String) key);
            field.setAccessible(true);
            if (key.equals("password"))
                value = new BCryptPasswordEncoder().encode(value.toString());
            if (key.equals("updatedAt"))
                value = new Date();
            ReflectionUtils.setField(field, userDTODB, value);
        });

        User user = this.repository.save(toUser(userDTODB));
        LOGGER.info("UserDTO-updatePartial -> {}", user);

        return this.toUserDTO(user);
    }

    @Override
    public boolean delete(String id) {
        if (!this.existsById(id)) {
            LOGGER.info("UserDTO not found -> {}", id);
            return false;
        }
        this.repository.deleteById(id);
        LOGGER.info("UserDTO deleted -> {}", id);
        return true;
    }

    @Override
    public boolean existsById(String id) {
        return this.repository.existsById(id);
    }

    //add access

    @Override
    public UserDTO saveAdmin(UserDTO userDTO) {

        Optional<User> userDB = this.repository.findByRoles(Roles.getAdminAccess());

        if (userDB.isPresent())
            return null;

        userDTO.setRoles(Roles.getAdminAccess());
        userDTO.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        userDTO.setCreatedAt(new Date());
        userDTO.setUpdatedAt(new Date());

        User user = this.repository.save(this.toUser(userDTO));
        LOGGER.info("UserAdmin-save -> {}", user);
        return this.toUserDTO(user);
    }

    @Override
    public UserDTO accessUser(String id) {
        UserDTO userDTO = this.findById(id);

        User userDB = this.repository.findByIdAndRoles(id, userDTO.getRoles()).orElse(null);

        if (null == userDB)
            return null;

        userDB.setRoles(Roles.getUserAccess());
        userDB.setUpdatedAt(new Date());

        User user = this.repository.save(userDB);
        LOGGER.info("UserDTO-accessUser -> {}", user);

        return this.toUserDTO(user);
    }

    @Override
    public UserDTO accessModerator(String id) {

        UserDTO userDTO = this.findById(id);

        User userDB = this.repository.findByIdAndRoles(id, userDTO.getRoles()).orElse(null);

        if (null == userDB)
            return null;

        userDB.setRoles(Roles.getModeratorAccess());
        userDB.setUpdatedAt(new Date());

        User user = this.repository.save(userDB);
        LOGGER.info("UserDTO-accessModerator -> {}", user);

        return this.toUserDTO(user);
    }


    /**
     * {@summary Mapper entity to dto.}
     *
     * @param user entity
     * @return UserDTO dtos
     */
    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setRoles(user.getRoles());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }

    /**
     * {@summary Mapper dto to entity.}
     *
     * @param userDTO dtos
     * @return User entity
     */
    public User toUser(UserDTO userDTO) {
        User ud = new User();
        ud.setId(userDTO.getId());
        ud.setName(userDTO.getName());
        ud.setUsername(userDTO.getUsername());
        ud.setPassword(userDTO.getPassword());
        ud.setRoles(userDTO.getRoles());
        ud.setCreatedAt(userDTO.getCreatedAt());
        ud.setUpdatedAt(userDTO.getUpdatedAt());
        return ud;
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
