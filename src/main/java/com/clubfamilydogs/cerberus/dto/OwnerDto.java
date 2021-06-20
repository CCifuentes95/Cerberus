package com.clubfamilydogs.cerberus.dto;

import com.clubfamilydogs.cerberus.persistance.models.Pet;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"createdBy", "modifiedBy", "createdDate", "modifiedDate"})
public class OwnerDto {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private Set<PetDto> pets;

    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public void addPet(PetDto pet) {
        this.pets = CollectionUtils.isEmpty(pets) ? new HashSet<>() : pets;
        this.pets.add(pet);
        pet.setOwnerId(id);
    }
}
