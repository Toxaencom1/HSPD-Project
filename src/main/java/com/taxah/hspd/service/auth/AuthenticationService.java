package com.taxah.hspd.service.auth;

import com.taxah.hspd.dto.JwtRequestDTO;
import com.taxah.hspd.dto.JwtResponseDTO;
import com.taxah.hspd.dto.LoginRequestDTO;
import com.taxah.hspd.dto.RegisterRequestDTO;
import com.taxah.hspd.entity.auth.RefreshToken;
import com.taxah.hspd.entity.auth.Role;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.enums.Roles;
import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.repository.auth.RoleRepository;
import com.taxah.hspd.service.auth.impl.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.taxah.hspd.util.constant.Exceptions.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public JwtResponseDTO refreshAccessToken(JwtRequestDTO requestDTO) {
        RefreshToken refreshToken = refreshTokenService.getRefreshToken(requestDTO.getRefreshToken());
        String userName = jwtService.extractUserName(requestDTO.getRefreshToken());
        UserDetails userDetails = userService.loadUserByUsername(userName);
        boolean tokenValid = jwtService.isTokenValid(refreshToken.getRefreshToken(), userDetails);

        if (tokenValid) {
            return JwtResponseDTO.builder()
                    .token(jwtService.generateToken(userDetails))
                    .refreshToken(requestDTO.getRefreshToken())
                    .build();
        }
        throw new AuthorizationDeniedException(INVALID_REFRESH_TOKEN);
    }

    public JwtResponseDTO signUp(RegisterRequestDTO request) {
        UserDetails user = registerUser(request);
        return returnTokens(user);
    }

    public JwtResponseDTO signIn(LoginRequestDTO request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        var user = userService.loadUserByUsername(request.getUsername());
        return returnTokens(user);
    }

    public UserDetails registerUser(RegisterRequestDTO credentials) {
        if (!userService.existsByUsername(credentials.getUsername())) {
            if (!userService.existsByEmail(credentials.getEmail())) {
                Role userRole = roleRepository.findByRoles(Roles.USER);
                User user = User.builder()
                        .username(credentials.getUsername())
                        .password(passwordEncoder.encode(credentials.getPassword()))
                        .email(credentials.getEmail())
                        .roles(List.of(userRole))
                        .build();
                return userService.save(user);
            } else
                throw new AlreadyExistsException(EMAIL_ALREADY_EXISTS);
        } else
            throw new AlreadyExistsException(USERNAME_ALREADY_EXISTS);
    }

    @CachePut(value = "users", key = "#user.username.toLowerCase()")
    public JwtResponseDTO returnTokens(UserDetails user) {
        var jwtAccess = jwtService.generateToken(user);
        var jwtRefresh = jwtService.generateRefreshToken(user);
        refreshTokenService.saveRefreshToken(RefreshToken.builder().refreshToken(jwtRefresh).build());
        return JwtResponseDTO.builder()
                .token(jwtAccess)
                .refreshToken(jwtRefresh)
                .build();
    }
}
