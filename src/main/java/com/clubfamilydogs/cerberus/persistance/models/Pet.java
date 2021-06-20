package com.clubfamilydogs.cerberus.persistance.models;

import com.clubfamilydogs.cerberus.common.domain.AuditEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString
@Table(name = "pets")
public class Pet extends AuditEntity {

    private String name;
    private String lastName;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;


    @Builder
    public Pet(Long id, String createdBy, String modifiedBy, LocalDateTime createdDate, LocalDateTime modifiedDate, String name, String lastName, Owner owner) {
        super(id, createdBy, modifiedBy, createdDate, modifiedDate);
        this.name = name;
        this.lastName = lastName;
        this.owner = owner;
    }

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = -6422918369089830040L;
}
