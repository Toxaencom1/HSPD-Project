package com.taxah.hspd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taxah.hspd.dto.LoginRequestDTO;
import com.taxah.hspd.dto.RegisterRequestDTO;
import com.taxah.hspd.exception.AlreadyExistsException;
import com.taxah.hspd.service.auth.AuthenticationService;
import com.taxah.hspd.util.constant.Exceptions;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.taxah.hspd.util.constant.Endpoints.API_USER_LOGIN;
import static com.taxah.hspd.util.constant.Endpoints.API_USER_REGISTER;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class AuthControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Test
    void testSignUp_Success() throws Exception {
        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("username")
                .password("password")
                .email("email@email.com")
                .build();
        String requestJson = asJsonString(request);

        mockMvc.perform(post(API_USER_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    void testSignUp_Fail_Bad_Credentials() throws Exception {
        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("us")
                .password("pa")
                .email("email")
                .build();
        String requestJson = asJsonString(request);

        mockMvc.perform(post(API_USER_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSignUp_Fail_Already_Exist() throws Exception {
        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("username")
                .password("password2")
                .email("email2@email.com")
                .build();
        String requestJson = asJsonString(request);

        doThrow(new AlreadyExistsException(Exceptions.USERNAME_ALREADY_EXISTS))
                .when(authenticationService).signUp(request);

        mockMvc.perform(post(API_USER_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict());
    }

    @Test
    void testSignIn_Success() throws Exception {
        LoginRequestDTO request = LoginRequestDTO.builder().username("username").password("password").build();
        String requestJson = asJsonString(request);

        mockMvc.perform(post(API_USER_LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void testSignIn_Fail_Bad_Credentials() throws Exception {
        LoginRequestDTO request = LoginRequestDTO.builder().username("us").password("pa").build();
        String requestJson = asJsonString(request);

        mockMvc.perform(post(API_USER_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    private String asJsonString(final Object obj) {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }
}