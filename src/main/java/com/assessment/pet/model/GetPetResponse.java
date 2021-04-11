package com.assessment.pet.model;

import lombok.Data;

@Data
public class GetPetResponse {
    private String petId;
    private String name;
    private String type;
    private Long price;

    public GetPetResponse(String petId, Pet pet) {
        this.petId = petId;
        this.name = pet.getName();
        this.type = pet.getType();
        this.price = pet.getPrice();
    }
}
