package com.clubfamilydogs.cerberus.web.controller;

import com.clubfamilydogs.cerberus.dto.PetDto;
import com.clubfamilydogs.cerberus.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
class PetControllerIT {
    private final static String URI = "/api/pets";
    private final static String URI_ID = "/api/pets/{id}";

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private PetService petService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void index() throws Exception {

        List<PetDto> pets = List.of(
                PetDto.builder().id(1L).name("Apolo").lastName("Cifuentes").build(),
                PetDto.builder().id(2L).name("Tommy").lastName("Nuñez").build()
        );

        given(petService.findAll()).willReturn(pets);

        mockMvc.perform(get(URI)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {
        Optional<PetDto> pet = Optional.of(
                PetDto.builder().id(1L).name("Apolo").lastName("Cifuentes").build()
        );

        given(petService.findById(anyLong())).willReturn(pet);

        mockMvc.perform(get(URI_ID, 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void create() throws Exception {
        //Given
        PetDto pet = PetDto.builder().name("Ginebra").lastName("Cifuentes").build();
        PetDto savedPet = PetDto.builder().id(1L).name("Ginebra").lastName("Cifuentes").build();

        //When
        given(petService.save(any())).willReturn(savedPet);

        mockMvc.perform(post(URI)
                .content(mapper.writeValueAsString(pet))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedPet.getId()))
                .andExpect(jsonPath("$.name").value(savedPet.getName()))
                .andExpect(jsonPath("$.lastName").value(savedPet.getLastName()))
        ;
    }

    @Test
    void update() throws Exception {
        //Given
        PetDto updatedValue = PetDto.builder().id(1L).name("Artemis").lastName("Cifuentes").build();
        PetDto previous = PetDto.builder().id(1L).name("Artemiz").lastName("Cifuentes").build();
        PetDto savedPet = PetDto.builder().id(1L).name("Artemis").lastName("Cifuentes").build();

        //When
        given(petService.findById(anyLong())).willReturn(Optional.ofNullable(previous));
        given(petService.update(anyLong(), any())).willReturn(savedPet);

        mockMvc.perform(put(URI_ID, 1)
                .content(mapper.writeValueAsString(updatedValue))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedPet.getId()))
                .andExpect(jsonPath("$.name").value(savedPet.getName()))
                .andExpect(jsonPath("$.lastName").value(savedPet.getLastName()));
    }

    @Test
    void deletePet() throws Exception{
        Optional<PetDto> pet = Optional.of(
                PetDto.builder().id(1L).name("Apolo").lastName("Cifuentes").build()
        );

        given(petService.findById(anyLong())).willReturn(pet);

        mockMvc.perform(delete(URI_ID, 1))
                .andExpect(status().isOk());
    }

}