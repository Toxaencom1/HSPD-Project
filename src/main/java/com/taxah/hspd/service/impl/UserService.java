package com.taxah.hspd.service.impl;

import com.taxah.hspd.dto.RegisterRequestDTO;
import com.taxah.hspd.entity.Role;
import com.taxah.hspd.entity.User;
import com.taxah.hspd.enums.Roles;
import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.repository.RoleRepository;
import com.taxah.hspd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public User registerUser(RegisterRequestDTO credentials) {
        if (!userRepository.existsByUsernameIgnoreCase(credentials.getUsername())) {
            if (!userRepository.existsByEmailIgnoreCase(credentials.getEmail())) {
                Role userRole = roleRepository.findByRoles(Roles.USER);
                User user = User.builder()
                        .username(credentials.getUsername())
                        .password(passwordEncoder.encode(credentials.getPassword()))
                        .email(credentials.getEmail())
                        .roles(List.of(userRole))
                        .build();
                return userRepository.save(user);
            } else
                throw new AlreadyExistsException("Email already exists");
        } else
            throw new AlreadyExistsException("Username already exists");
    }
}
