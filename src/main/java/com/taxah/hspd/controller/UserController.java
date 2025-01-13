package com.taxah.hspd.controller;

import com.taxah.hspd.entity.auth.Role;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.enums.Roles;
import com.taxah.hspd.exception.NotFoundException;
import com.taxah.hspd.repository.auth.RoleRepository;
import com.taxah.hspd.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Deprecated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ROLE_NOT_FOUND = "Role %s not found";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('get_user_permission')")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> byId = userRepository.findById(id);
        return byId.map(user -> ResponseEntity.ok().body(user)).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('post_user_permission')")
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
                throw new NotFoundException(USER_NOT_FOUND);
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(String.format(ROLE_NOT_FOUND, role));
        }
    }
}
