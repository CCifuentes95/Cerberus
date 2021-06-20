package com.clubfamilydogs.cerberus.web.controller;

import com.clubfamilydogs.cerberus.dto.PetDto;
import com.clubfamilydogs.cerberus.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PetDto>> index() {
        return ResponseEntity.ok(petService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDto> findById(@PathVariable Long id) {
        Optional<PetDto> response = petService.findById(id);
        return ResponseEntity.of(response);
    }

    @PostMapping
    public ResponseEntity<PetDto> create(@Valid @RequestBody PetDto petDto) {
        return ResponseEntity.ok(petService.save(petDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> update( @PathVariable Long id, @Valid @RequestBody PetDto petDto) {
        return ResponseEntity.ok(petService.update(id, petDto));
    }

    @DeleteMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        petService.deleteById(id);
    }
}
