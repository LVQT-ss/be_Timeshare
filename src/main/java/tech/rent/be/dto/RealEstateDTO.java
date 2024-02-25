package tech.rent.be.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import tech.rent.be.entity.Resource;
//import tech.rent.be.entity.Resource;

import java.util.List;

@Data
public class RealEstateDTO {

    Long Id;
    String name;
    String description;
    String location;
    String type;

    List<ResourceDTO> resources;

}