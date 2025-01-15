package com.taxah.hspd.controller;

import com.taxah.hspd.dto.JwtResponse;
import com.taxah.hspd.dto.LoginRequestDTO;
import com.taxah.hspd.dto.RegisterRequestDTO;
import com.taxah.hspd.service.auth.AuthenticationService;
import com.taxah.hspd.utils.constant.Endpoints;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.API_USER)
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping(Endpoints.REGISTER)
    public JwtResponse signUp(@RequestBody @Valid RegisterRequestDTO request) {
        return authenticationService.signUp(request);
    }

    @PostMapping(Endpoints.LOGIN)
    public JwtResponse signIn(@RequestBody @Valid LoginRequestDTO request) {
        return authenticationService.signIn(request);
    }
}
