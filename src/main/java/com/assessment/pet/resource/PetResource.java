package com.assessment.pet.resource;

import com.assessment.pet.model.GetPetResponse;
import com.assessment.pet.model.Pet;
import com.assessment.pet.model.CreatePetResponse;
import com.assessment.pet.model.UpdatePetRequest;
import com.assessment.pet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/pet")
public class PetResource {

    private final PetService petService;

    @Autowired
    PetResource(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<CreatePetResponse> createPet(@RequestBody @NotNull @Valid Pet pet) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.createPet(pet));
    }

    @GetMapping("/{petId}")
    public ResponseEntity getPet(@PathVariable("petId") @NotBlank String petId) {
        GetPetResponse petResponse = petService.getPet(petId);
        return petResponse == null ? ResponseEntity.badRequest()
                .body("pet with petId: " + petId + " not found in records")
                : ResponseEntity.ok().body(petResponse);
    }

    @GetMapping
    public ResponseEntity<List<GetPetResponse>> getPets(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type) {
        List<GetPetResponse> pets = petService.getPets(name, type);
        return ResponseEntity.ok().body(pets);
    }

    @PutMapping("/{petId}")
    public ResponseEntity updatePet(@PathVariable("petId") @NotBlank String petId,
                                    @NotNull @RequestBody UpdatePetRequest updatePetRequest) {
        return petService.updatePet(petId, updatePetRequest);
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity deletePet(@PathVariable("petId") @NotBlank String petId) {
        return petService.deletePet(petId);
    }
}
