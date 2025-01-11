package com.taxah.hspd.service.auth;

import com.taxah.hspd.dto.JwtResponse;
import com.taxah.hspd.dto.LoginRequestDTO;
import com.taxah.hspd.dto.RegisterRequestDTO;
import com.taxah.hspd.entity.auth.Role;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.enums.Roles;
import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.repository.auth.RoleRepository;
import com.taxah.hspd.repository.auth.UserRepository;
import com.taxah.hspd.service.auth.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public JwtResponse signUp(RegisterRequestDTO request) {
        User user = registerUser(request);
        var jwt = jwtService.generateToken(user);
        return new JwtResponse(jwt);
    }

    public JwtResponse signIn(LoginRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService.loadUserByUsername(request.getUsername());
        var jwt = jwtService.generateToken(user);
        return new JwtResponse(jwt);
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
