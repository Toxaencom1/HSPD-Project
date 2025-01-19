package com.taxah.hspd.controller;

import com.taxah.hspd.controller.doc.AuthControllerSwagger;
import com.taxah.hspd.dto.JwtResponse;
import com.taxah.hspd.dto.LoginRequestDTO;
import com.taxah.hspd.dto.RegisterRequestDTO;
import com.taxah.hspd.service.auth.AuthenticationService;
import com.taxah.hspd.util.constant.Endpoints;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoints.API_USER)
@RequiredArgsConstructor
public class AuthController implements AuthControllerSwagger {
    private final AuthenticationService authenticationService;

    @PostMapping(Endpoints.REGISTER)
    public ResponseEntity<JwtResponse> signUp(@RequestBody @Valid RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.signUp(request));
    }

    @PostMapping(Endpoints.LOGIN)
    public ResponseEntity<JwtResponse> signIn(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.signIn(request));
    }
}
