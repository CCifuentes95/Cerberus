package com.clubfamilydogs.cerberus.mapper;

import com.clubfamilydogs.cerberus.dto.PetDto;
import com.clubfamilydogs.cerberus.persistance.models.Owner;
import com.clubfamilydogs.cerberus.persistance.models.Pet;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Objects;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring")
public interface PetMapper {

    default Owner setOwnerId(PetDto petDto) {
        return (Objects.nonNull(petDto) && Objects.nonNull(petDto.getOwnerId())) ?
                Owner.builder().id(petDto.getOwnerId()).build() : null;
    }

    @Mapping(target = "owner", expression = "java(setOwnerId(dto))")
    Pet toEntity(PetDto dto);

    @Mapping(target = "ownerId", source = "owner.id")
    PetDto toDto(Pet entity);

    List<Pet> toEntities(List<PetDto> dtos);

    List<PetDto> toDtos(List<Pet> entities);
}
