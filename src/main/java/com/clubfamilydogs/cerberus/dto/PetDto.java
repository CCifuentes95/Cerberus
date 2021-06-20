package com.clubfamilydogs.cerberus.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"createdBy", "modifiedBy", "createdDate", "modifiedDate"})
public class PetDto{
    private Long id;
    private String name;
    private String lastName;
    private Long ownerId;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
