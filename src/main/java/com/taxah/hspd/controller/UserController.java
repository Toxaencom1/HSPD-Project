package com.taxah.hspd.controller;

import com.taxah.hspd.entity.Role;
import com.taxah.hspd.entity.User;
import com.taxah.hspd.enums.Roles;
import com.taxah.hspd.exception.UserNotFoundException;
import com.taxah.hspd.repository.RoleRepository;
import com.taxah.hspd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('get_user_permission')")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> byId = userRepository.findById(id);
        return byId.map(user -> ResponseEntity.ok().body(user)).orElseThrow(()->new UserNotFoundException("User not found"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('post_user_permission')")
    public ResponseEntity<?> createUser(@RequestParam(value = "role") String role, @RequestBody User user) {
        try {
            Roles roles = Roles.valueOf(role);
            Role byRoles = roleRepository.findByRoles(roles);
            user.setRoles(List.of(byRoles));
            User savedUser = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role " + role + " not found");
        }
    }
}
