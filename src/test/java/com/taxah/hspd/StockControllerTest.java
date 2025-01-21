package com.taxah.hspd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taxah.hspd.dto.GetStockResponseDataDTO;
import com.taxah.hspd.entity.auth.Permission;
import com.taxah.hspd.entity.auth.Role;
import com.taxah.hspd.entity.auth.User;
import com.taxah.hspd.entity.polygonAPI.Result;
import com.taxah.hspd.entity.polygonAPI.StockResponseData;
import com.taxah.hspd.enums.Roles;
import com.taxah.hspd.repository.auth.UserRepository;
import com.taxah.hspd.repository.polygonAPI.ResultRepository;
import com.taxah.hspd.service.pilygonAPI.PolygonService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static com.taxah.hspd.util.constant.Endpoints.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class StockControllerTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ResultRepository resultRepository;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PolygonService polygonService;

    Long userId;

    @BeforeEach
    void setUp() {
        resultRepository.deleteAll();
        userRepository.deleteAll();
        Permission userPermission = Permission.builder().name("user_permission").build();
        Role role = Role.builder().roles(Roles.USER).build();
        userPermission.setRoles(List.of(role));
        User user = User.builder()
                .username("user")
                .password("user")
                .email("user@mail.test")
                .roles(List.of(role))
                .build();
        User saved = userRepository.save(user);
        userId = saved.getId();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testSaveStocks_Success() throws Exception {
        GetStockResponseDataDTO dataDTO = GetStockResponseDataDTO.builder()
                .ticker("AAPL")
                .start(LocalDate.of(2023, 1, 1))
                .end(LocalDate.of(2023, 2, 1))
                .build();
        String requestJson = asJsonString(dataDTO);
        StockResponseData data = objectMapper.readValue(new File("src/test/resources/json/ApiResults.json"), StockResponseData.class);

        when(polygonService.getNewApiResults(dataDTO)).thenReturn(data.getResults());
        when(polygonService.checkTickerInPolygon(dataDTO.getTicker())).thenReturn(true);

        mockMvc.perform(post(API_DATA + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());

        List<Result> results = resultRepository.findResultsByUserAndTicker(userId, dataDTO.getTicker());

        assertNotNull(results);
        assertEquals(data.getResults().size(), results.size());
    }

    @Test
    @WithMockUser
    void testSaveStocks_Fail_InvalidData() throws Exception {
        GetStockResponseDataDTO dataDTO = GetStockResponseDataDTO.builder()
                .build();
        String requestJson = asJsonString(dataDTO);

        when(polygonService.checkTickerInPolygon(dataDTO.getTicker())).thenReturn(false);

        mockMvc.perform(post(API_DATA + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testSaveStocks_Fail_InvalidTicker() throws Exception {
        GetStockResponseDataDTO dataDTO = GetStockResponseDataDTO.builder()
                .ticker("AAPLsdfgsdfg")
                .start(LocalDate.of(2023, 1, 1))
                .end(LocalDate.of(2023, 2, 1))
                .build();
        String requestJson = asJsonString(dataDTO);

        when(polygonService.checkTickerInPolygon(dataDTO.getTicker())).thenReturn(false);

        mockMvc.perform(post(API_DATA + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser
    void testGetSavedInfoByTicker_Success() throws Exception {
        String ticker = "AAPL";
        GetStockResponseDataDTO dataDTO = GetStockResponseDataDTO.builder()
                .ticker(ticker)
                .start(LocalDate.of(2023, 1, 1))
                .end(LocalDate.of(2023, 2, 1))
                .build();
        String requestJson = asJsonString(dataDTO);
        StockResponseData data = objectMapper.readValue(new File("src/test/resources/json/ApiResults.json"), StockResponseData.class);

        when(polygonService.getNewApiResults(dataDTO)).thenReturn(data.getResults());
        when(polygonService.checkTickerInPolygon(ticker)).thenReturn(true);

        mockMvc.perform(post(API_DATA + SAVE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());


        mockMvc.perform(get(API_DATA + FETCH)
                        .param("ticker", ticker)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        List<Result> results = resultRepository.findResultsByUserAndTicker(userId, dataDTO.getTicker());

        assertNotNull(results);
        assertEquals(data.getResults().size(), results.size());
    }

    @Test
    @WithMockUser
    void testGetSavedInfoByTicker_Fail_TickerNotFound() throws Exception {
        String ticker = "UNKNOWN";

        when(polygonService.checkTickerInPolygon(ticker)).thenReturn(false);

        mockMvc.perform(get(API_DATA + FETCH)
                        .param("ticker", ticker)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    private String asJsonString(final Object obj) {
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }
}
