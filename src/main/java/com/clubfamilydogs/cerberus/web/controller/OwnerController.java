package com.clubfamilydogs.cerberus.web.controller;

import com.clubfamilydogs.cerberus.dto.OwnerDto;
import com.clubfamilydogs.cerberus.service.OwnerService;
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
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OwnerDto>> index() {
        return ResponseEntity.ok(ownerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> findById(@PathVariable Long id) {
        Optional<OwnerDto> response = ownerService.findById(id);
        return ResponseEntity.of(response);
    }

    @PostMapping
    public ResponseEntity<OwnerDto> create(@Valid @RequestBody OwnerDto ownerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ownerService.save(ownerDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerDto> update( @PathVariable Long id, @Valid @RequestBody OwnerDto ownerDto) {
        return ResponseEntity.ok(ownerService.update(id, ownerDto));
    }

    @DeleteMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        ownerService.deleteById(id);
    }
}
