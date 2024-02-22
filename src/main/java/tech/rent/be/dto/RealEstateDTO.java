package tech.rent.be.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
//import tech.rent.be.entity.Resource;

import java.util.List;

@Data
public class RealEstateDTO {

    Long Id;
    String name;
    String description;
    String location;
    String type;

    Long userId;

//    List<Resource> resources;
    
    public void setId(Long id) {
        Id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

//    public List<Resource> getResources() {
//        return resources;
//    }
//
//    public void setResources(List<Resource> resources) {
//        this.resources = resources;
//    }

    public Long getUserId() {
        return userId;
    }

    public Long getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }
}