package com.taxah.hspd.controller;

import com.taxah.hspd.dto.JwtResponse;
import com.taxah.hspd.dto.LoginRequestDTO;
import com.taxah.hspd.dto.RegisterRequestDTO;
import com.taxah.hspd.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public JwtResponse signUp(@RequestBody @Valid RegisterRequestDTO request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/login")
    public JwtResponse signIn(@RequestBody @Valid LoginRequestDTO request) {
        return authenticationService.signIn(request);
    }
}
