package com.clubfamilydogs.cerberus.service;

import com.clubfamilydogs.cerberus.dto.OwnerDto;
import com.clubfamilydogs.cerberus.mapper.OwnerMapper;
import com.clubfamilydogs.cerberus.persistance.OwnerRepository;
import com.clubfamilydogs.cerberus.persistance.models.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerMapper ownerMapper;
    private final OwnerRepository ownerRepository;

    public List<OwnerDto> findAll() {
        return ownerMapper.toDtos(ownerRepository.findAll());
    }

    public Optional<OwnerDto> findById(Long id){
        return ownerRepository.findById(id).map(ownerMapper::toDto);
    }

    public OwnerDto save(OwnerDto ownerDto){

        Owner owner = ownerMapper.toEntity(ownerDto);
        setOwner(owner);

        Owner saved = ownerRepository.saveAndFlush(owner);

        return ownerMapper.toDto(saved);
    }

    private void setOwner(Owner owner) {
        if (Objects.nonNull(owner.getPets())) {
            owner.getPets().forEach(pet -> pet.setOwner(owner));
        }
    }

    public OwnerDto update(Long id, OwnerDto ownerDto){
        Objects.requireNonNull(id, "Id is required");

        ownerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not Found"));

        setOwnerId(ownerDto);

        Owner ownerUpdated = ownerMapper.toEntity(ownerDto);


        ownerUpdated = ownerRepository.saveAndFlush(ownerUpdated);

        return ownerMapper.toDto(ownerUpdated);
    }

    private void setOwnerId(OwnerDto ownerDto) {
        if (Objects.nonNull(ownerDto.getPets())) {
            ownerDto.getPets().forEach(pet -> pet.setOwnerId(ownerDto.getId()));
        }
    }

    public void deleteById(Long id){
        ownerRepository.deleteById(id);
    }
}
