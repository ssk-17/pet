package com.assessment.pet.service;

import com.assessment.pet.model.GetPetResponse;
import com.assessment.pet.model.Pet;
import com.assessment.pet.model.CreatePetResponse;
import com.assessment.pet.model.UpdatePetRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PetService {
    CreatePetResponse createPet(Pet pet);

    GetPetResponse getPet(String petId);

    List<GetPetResponse> getPets(String name, String type);

    ResponseEntity updatePet(String petId, UpdatePetRequest updatePetRequest);

    ResponseEntity deletePet(String petId);
}
