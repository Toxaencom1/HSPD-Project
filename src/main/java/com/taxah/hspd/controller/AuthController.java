package com.taxah.hspd.controller;

import com.taxah.hspd.controller.doc.AuthControllerSwagger;
import com.taxah.hspd.dto.JwtRequestDTO;
import com.taxah.hspd.dto.JwtResponseDTO;
import com.taxah.hspd.dto.LoginRequestDTO;
import com.taxah.hspd.dto.RegisterRequestDTO;
import com.taxah.hspd.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.taxah.hspd.util.constant.Endpoints.*;

@RestController
@RequestMapping(API_USER)
@RequiredArgsConstructor
public class AuthController implements AuthControllerSwagger {
    private final AuthenticationService authenticationService;

    @PostMapping(REGISTER)
    public ResponseEntity<JwtResponseDTO> signUp(@RequestBody @Valid RegisterRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.signUp(request));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<JwtResponseDTO> signIn(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.signIn(request));
    }

    @PostMapping(REFRESH)
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody JwtRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.refreshAccessToken(requestDTO));
    }
}
