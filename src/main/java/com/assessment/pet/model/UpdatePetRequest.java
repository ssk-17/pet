package com.assessment.pet.model;

import lombok.Data;

@Data
public class UpdatePetRequest {
    private String name;
    private Long price;
}
