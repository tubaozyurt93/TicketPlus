package com.patika.ticketplusservice.controller;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.patika.ticketplusservice.model.enums.VehicleType;
import com.patika.ticketplusservice.request.VehicleRequest;
import com.patika.ticketplusservice.response.VehicleResponse;
import com.patika.ticketplusservice.service.VehicleService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(value = "integration")
public class VehicleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void it_should_get_all_vehicles() throws Exception{
        //given
        //when
        //then
    }


    @Test
    void it_should_create_vehicle_when_request_body() throws Exception {

        //given
        VehicleRequest request = VehicleRequest.builder()
                .vehicleType(VehicleType.BUS)
                .capacity(45)
                .build();

        VehicleResponse response = VehicleResponse.builder()
                .vehicleType(VehicleType.BUS)
                .capacity(45)
                .build();

        //when
        when(vehicleService.create(request)).thenReturn(response);
        mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serializeJson(request)))
                .andDo(print())
                .andExpect(jsonPath("$.capacity").value(request.getCapacity()));

    }

    private String serializeJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }


}
