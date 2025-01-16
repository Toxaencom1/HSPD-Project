package com.taxah.hspd.controller;

import com.taxah.hspd.entity.auth.Role;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.enums.Roles;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.repository.auth.RoleRepository;
import com.taxah.hspd.repository.auth.UserRepository;
import com.taxah.hspd.util.constant.Endpoints;
import com.taxah.hspd.util.constant.Exceptions;
import com.taxah.hspd.util.constant.Security;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Deprecated
@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoints.API_USER)
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping(Endpoints.PATH_VARIABLE_ID)
    @PreAuthorize(Security.USER_AUTHORITY)
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> byId = userRepository.findById(id);
        return byId.map(user -> ResponseEntity.ok().body(user)).orElseThrow(() -> new NotFoundException(Exceptions.USER_NOT_FOUND));
    }

    @PostMapping
    @PreAuthorize(Security.ADD_ROLE_AUTHORITY)
    public ResponseEntity<User> addRoleToUser(@RequestParam String role, @RequestParam String username) {
        try {
            Roles roles = Roles.valueOf(role);
            Optional<User> user = userRepository.findByUsernameIgnoreCase(username);
            if (user.isPresent()) {
                Role existingRole = roleRepository.findByRoles(roles);
                user.get().addRole(existingRole);
                User savedUser = userRepository.save(user.get());
                return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
            } else
                throw new NotFoundException(String.format(Exceptions.USER_NOT_FOUND_FORMATTED, username));
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(String.format(Exceptions.ROLE_NOT_FOUND_FORMATTED, role));
        }
    }
}
