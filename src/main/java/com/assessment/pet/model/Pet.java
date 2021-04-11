package com.assessment.pet.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Pet {
    @NotBlank
    private String name;
    @NotBlank
    private String type;
    @NotNull
    private Long price;
}
