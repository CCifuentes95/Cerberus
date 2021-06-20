package com.clubfamilydogs.cerberus.persistance.models;

import com.clubfamilydogs.cerberus.common.domain.AuditEntity;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString
@Table(name = "owners")
public class Owner extends AuditEntity {
    private String name;
    private String lastName;
    private String email;

    @ToString.Exclude
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Pet> pets = new HashSet<>();

    public void addPet(Pet pet) {
        this.pets = CollectionUtils.isEmpty(pets) ? new HashSet<>() : pets;
        this.pets.add(pet);
        pet.setOwner(this);
    }

    @Builder
    public Owner(Long id, String createdBy, String modifiedBy, LocalDateTime createdDate, LocalDateTime modifiedDate, String name, String lastName, String email, Set<Pet> pets) {
        super(id, createdBy, modifiedBy, createdDate, modifiedDate);
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.pets = pets;
    }

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 7077273934872355052L;
}
