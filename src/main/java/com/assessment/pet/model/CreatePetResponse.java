package com.assessment.pet.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePetResponse {
    private String petId;
}
