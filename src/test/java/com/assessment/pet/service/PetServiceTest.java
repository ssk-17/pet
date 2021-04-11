package com.assessment.pet.service;

import com.assessment.pet.model.GetPetResponse;
import com.assessment.pet.model.Pet;
import com.assessment.pet.model.CreatePetResponse;
import com.assessment.pet.model.UpdatePetRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public class PetServiceTest {

    private PetService petService = new PetServiceImpl();

    private final long price = 345;
    private final String name = "juby";
    private final String type = "dog";

    @Test
    void testCreatePet() {
        Pet pet = Pet.builder().price(price).name(name).type(type).build();
        CreatePetResponse response = petService.createPet(pet);
        Assertions.assertNotNull(response.getPetId());
    }

    @Test
    void testGetPet() {
        Pet pet = Pet.builder().price(price).name(name).type(type).build();
        CreatePetResponse response = petService.createPet(pet);
        GetPetResponse getPetResponse = petService.getPet(response.getPetId());

        Assertions.assertNotNull(getPetResponse);
        Assertions.assertEquals(response.getPetId(), getPetResponse.getPetId());
        Assertions.assertEquals(price, getPetResponse.getPrice());
        Assertions.assertEquals(name, getPetResponse.getName());
        Assertions.assertEquals(type, getPetResponse.getType());
    }

    @Test
    void testGetPetWithInvalidPetId() {
        Pet pet = Pet.builder().price(price).name(name).type(type).build();
        petService.createPet(pet);
        GetPetResponse getPetResponse = petService.getPet(UUID.randomUUID().toString());

        Assertions.assertNull(getPetResponse);
    }

    @Test
    void testGetPets() {
        createMultiplePets();
        List<GetPetResponse> response = petService.getPets(null, null);

        Assertions.assertEquals(4, response.size());
    }

    @Test
    void testGetPetsWithEmptyNameType() {
        createMultiplePets();
        List<GetPetResponse> response = petService.getPets("", "");

        Assertions.assertEquals(4, response.size());
    }

    @Test
    void testGetPetsWithFilters() {
        createMultiplePets();
        List<GetPetResponse> response = petService.getPets("JUBY", null);
        List<GetPetResponse> typeResponse = petService.getPets(null, "Dog");

        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals(2, typeResponse.size());
    }

    @Test
    void testUpdatePet() {
        CreatePetResponse createPetResponse = petService
                .createPet(Pet.builder().type(type).name(name).price(price).build());
        UpdatePetRequest updatePetRequest = new UpdatePetRequest();
        updatePetRequest.setPrice(456L);

        petService.updatePet(createPetResponse.getPetId(), updatePetRequest);

        //check if price is updated
        GetPetResponse getPetResponse = petService.getPet(createPetResponse.getPetId());
        Assertions.assertEquals(456L, getPetResponse.getPrice());
        Assertions.assertEquals(name, getPetResponse.getName());
        Assertions.assertEquals(type, getPetResponse.getType());
    }

    @Test
    void testUpdatePetWithInvalidPetId() {
        CreatePetResponse createPetResponse = petService
                .createPet(Pet.builder().type(type).name(name).price(price).build());
        UpdatePetRequest updatePetRequest = new UpdatePetRequest();
        updatePetRequest.setPrice(456L);

        ResponseEntity responseEntity = petService
                .updatePet(UUID.randomUUID().toString(), updatePetRequest);

        //check for bad request
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testUpdatePetWithInvalidUpdateRequest() {
        CreatePetResponse createPetResponse = petService
                .createPet(Pet.builder().type(type).name(name).price(price).build());
        UpdatePetRequest updatePetRequest = new UpdatePetRequest();

        ResponseEntity responseEntity = petService
                .updatePet(createPetResponse.getPetId(), updatePetRequest);

        //check for bad request
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals("both name and price should not be null in request", responseEntity.getBody());
    }

    @Test
    void testDeletePet() {
        createMultiplePets();
        List<GetPetResponse> petResponses = petService.getPets(null, null);
        Assertions.assertEquals(4, petResponses.size());
        String firstPetId = petResponses.get(0).getPetId();
        petService.deletePet(firstPetId);

        petResponses = petService.getPets(null, null);
        Assertions.assertEquals(3, petResponses.size());

        GetPetResponse getPetResponse = petService.getPet(firstPetId);
        Assertions.assertEquals(null, getPetResponse);
    }

    @Test
    void testDeletePetWithInvalidPetId() {
        createMultiplePets();
        List<GetPetResponse> petResponses = petService.getPets(null, null);
        Assertions.assertEquals(4, petResponses.size());
        String randomUUID = UUID.randomUUID().toString();
        ResponseEntity responseEntity = petService.deletePet(randomUUID);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Assertions.assertEquals(
                "petId: " + randomUUID + " not found in the records", responseEntity.getBody());
    }

    private void createMultiplePets() {
        petService.createPet(Pet.builder().price(price).name(name).type(type).build());
        petService.createPet(Pet.builder().price(123L).name("julia").type("cat").build());
        petService.createPet(Pet.builder().price(345L).name("snap").type("parrot").build());
        petService.createPet(Pet.builder().price(534L).name("juliet").type("dog").build());
    }
}
