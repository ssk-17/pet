package com.assessment.pet.service;

import com.assessment.pet.model.GetPetResponse;
import com.assessment.pet.model.Pet;
import com.assessment.pet.model.CreatePetResponse;
import com.assessment.pet.model.UpdatePetRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PetServiceImpl implements PetService {

    Map<String, GetPetResponse> map = new HashMap<>();

    @Override
    public CreatePetResponse createPet(Pet pet) {

        String randomId = UUID.randomUUID().toString();
        GetPetResponse getPetResponse = new GetPetResponse(randomId, pet);
        map.put(randomId, getPetResponse);

        return CreatePetResponse.builder().petId(randomId).build();
    }

    @Override
    public GetPetResponse getPet(String petId) {
        return map.getOrDefault(petId, null);
    }

    @Override
    public List<GetPetResponse> getPets(String name, String type) {
        List<GetPetResponse> filteredPetResponses = new ArrayList<>(map.values());

        if (name != null && !name.isEmpty())
            filteredPetResponses = filteredPetResponses.stream().filter(
                    getPetResponse -> name.equalsIgnoreCase(getPetResponse.getName())).collect(Collectors.toList());

        if (type != null && !type.isEmpty())
            filteredPetResponses = filteredPetResponses.stream().filter(
                    getPetResponse -> type.equalsIgnoreCase(getPetResponse.getType())).collect(Collectors.toList());

        return filteredPetResponses;
    }

    @Override
    public ResponseEntity updatePet(String petId, UpdatePetRequest updatePetRequest) {
        if (!map.containsKey(petId))
            return ResponseEntity.badRequest().body("petId: " + petId + " not found in the records");
        if (updatePetRequest.getName() == null && updatePetRequest.getPrice() == null)
            return ResponseEntity.badRequest().body("both name and price should not be null in request");

        GetPetResponse pet = map.get(petId);
        if (updatePetRequest.getName() != null) pet.setName(updatePetRequest.getName());
        if (updatePetRequest.getPrice() != null) pet.setPrice(updatePetRequest.getPrice());

        map.put(petId, pet);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity deletePet(String petId) {
        if (!map.containsKey(petId))
            return ResponseEntity.badRequest().body("petId: " + petId + " not found in the records");

        map.remove(petId);
        return ResponseEntity.noContent().build();
    }
}
