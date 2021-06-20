package com.clubfamilydogs.cerberus.mapper;

import com.clubfamilydogs.cerberus.dto.OwnerDto;
import com.clubfamilydogs.cerberus.dto.PetDto;
import com.clubfamilydogs.cerberus.persistance.models.Owner;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel ="spring", uses = {PetMapper.class})
public interface OwnerMapper {

    default Owner setOwnerId(Owner owner, PetDto petDto) {
        return (Objects.nonNull(petDto) && Objects.nonNull(petDto.getOwnerId())) ?
                Owner.builder().id(petDto.getOwnerId()).build() : null;
    }

    Owner toEntity(OwnerDto dto);
    OwnerDto toDto(Owner entity);
    List<Owner> toEntities(List<OwnerDto> dtos);
    List<OwnerDto> toDtos(List<Owner> entities);
}
