package tech.rent.be.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.rent.be.dto.PostResponseDTO;
import tech.rent.be.dto.RealEstateDTO;
import tech.rent.be.entity.RealEstate;
import tech.rent.be.services.RealEstateService;

import java.util.List;

@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class RealEstateController {
    @Autowired
    RealEstateService realEstateService;

    @PostMapping("/estate")
    public ResponseEntity estate(@RequestBody RealEstateDTO realEstateDTO){
        RealEstate realEstate = realEstateService.createEstate(realEstateDTO);
        return ResponseEntity.ok(realEstate);
    }
    @GetMapping("/showEstate")
    public  ResponseEntity<List<RealEstateDTO>> getAllRealEstate(){
       List<RealEstateDTO> estates = realEstateService.getAllRealEstate();
        return ResponseEntity.ok(estates);
    }




}