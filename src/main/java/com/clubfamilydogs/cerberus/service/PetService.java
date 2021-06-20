package com.clubfamilydogs.cerberus.service;

import com.clubfamilydogs.cerberus.mapper.PetMapper;
import com.clubfamilydogs.cerberus.persistance.models.Pet;
import com.clubfamilydogs.cerberus.dto.PetDto;
import com.clubfamilydogs.cerberus.persistance.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetMapper petMapper;
    private final PetRepository petRepository;

    public List<PetDto> findAll() {
        return petMapper.toDtos(petRepository.findAll());
    }

    /**
     * Searching a Pet given the ID
     * @param id the number to search
     * @return Pet or empty if not found
     */
    public Optional<PetDto> findById(Long id){
        return petRepository.findById(id).map(petMapper::toDto);
    }

    public PetDto save(PetDto petDto){
        Pet pet = petMapper.toEntity(petDto);

        Pet saved = petRepository.saveAndFlush(pet);

        return petMapper.toDto(saved);
    }

    public PetDto update(Long id, PetDto petDto){
        Objects.requireNonNull(id, "Id is required");

        petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        Pet petUpdated = petMapper.toEntity(petDto);

        petUpdated = petRepository.save(petUpdated);

        return petMapper.toDto(petUpdated);
    }

    public void deleteById(Long id){
        petRepository.deleteById(id);
    }
}
