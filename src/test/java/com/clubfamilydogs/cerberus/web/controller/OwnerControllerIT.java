package com.clubfamilydogs.cerberus.web.controller;

import com.clubfamilydogs.cerberus.CerberusApplication;
import com.clubfamilydogs.cerberus.dto.OwnerDto;
import com.clubfamilydogs.cerberus.dto.PetDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CerberusApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OwnerControllerIT {
    private final static String URI = "http://localhost:%d/api/owners";
    private final static String URI_ID = "http://localhost:%d/api/owners/%d";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
    }

    @Test
    void index() {
        var response = this.restTemplate.getForEntity(String.format(URI, port), OwnerDto[].class);
        var owners = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(owners).isNotNull();
    }

    @Test
    void findById() {
        var response = this.restTemplate.getForEntity(String.format(URI_ID, port, 1L), OwnerDto.class);
        var owner = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(owner).isNotNull();
    }

    @Test
    void create() {
        var owner = OwnerDto.builder().name("Beatriz").lastName("Grass").pets(Set.of(PetDto.builder().name("Simon").lastName("Grass").build())).build();
        var response = this.restTemplate.postForEntity(String.format(URI, port), owner, String.class);
        var createdOwner = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createdOwner).isNotNull();
    }

    @Test
    void update() {
        var ownerPayload = OwnerDto.builder().name("Carlos").lastName("Gras").pets(Set.of(PetDto.builder().name("Matilda").lastName("Grass").build())).build();
        var response = this.restTemplate.postForEntity(String.format(URI, port), ownerPayload, OwnerDto.class);
        var createdOwner = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createdOwner).isNotNull();
        assertThat(createdOwner.getId()).isNotNull();

        createdOwner.setLastName("Grass");
        this.restTemplate.put(String.format(URI_ID, port, createdOwner.getId()), ownerPayload, OwnerDto.class);

        var updatedResponse = this.restTemplate.getForEntity(String.format(URI_ID, port, 1L), OwnerDto.class);
        var updatedOwner = response.getBody();
        assertThat(updatedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updatedOwner).isNotNull();
        assertThat(updatedOwner.getLastName()).isNotNull().isEqualTo("Grass");

    }

    @Test
    void delete() {
        var ownerPayload = OwnerDto.builder().name("Sofia").lastName("Grass").pets(Set.of(PetDto.builder().name("Parker").lastName("Grass").build())).build();
        var response = this.restTemplate.postForEntity(String.format(URI, port), ownerPayload, OwnerDto.class);
        var createdOwner = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createdOwner).isNotNull();
        assertThat(createdOwner.getId()).isNotNull();

        this.restTemplate.delete(String.format(URI_ID, port, createdOwner.getId()), ownerPayload, OwnerDto.class);

        var deletedResponse = this.restTemplate.getForEntity(String.format(URI_ID, port, createdOwner.getId()), OwnerDto.class);
        assertThat(deletedResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}