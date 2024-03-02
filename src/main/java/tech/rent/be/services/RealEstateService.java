package tech.rent.be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import tech.rent.be.dto.PostResponseDTO;
import tech.rent.be.dto.RealEstateDTO;
import tech.rent.be.dto.ResourceDTO;
import tech.rent.be.entity.*;
//import tech.rent.be.entity.Resource;
import tech.rent.be.repository.CategoryRepository;
import tech.rent.be.repository.LocationRepository;
import tech.rent.be.repository.RealEstateRepository;
import tech.rent.be.repository.UsersRepository;
import tech.rent.be.utils.AccountUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RealEstateService {
    @Autowired
    RealEstateRepository realEstateRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    LocationRepository locationRepository;

    public RealEstate createEstate(RealEstateDTO realEstateDTO) {
        Users users = accountUtils.getCurrentUser();
        Category category = categoryRepository.findCategoryById(realEstateDTO.getCategoryId());
        Location location = locationRepository.findLocationById(realEstateDTO.getLocationId());
        RealEstate realEstate = new RealEstate();
        realEstate.setDescription(realEstateDTO.getDescription());
        realEstate.setTitle(realEstateDTO.getTitle());
        realEstate.setDescription(realEstateDTO.getDescription());
        realEstate.setDate(realEstateDTO.getDate());
        realEstate.setAmount(realEstateDTO.getAmount());
        realEstate.setCategory(category);
        realEstate.setLocation(location);
        realEstate.setUsers(users);
        List<RealEstate> realEstates = category.getEstates();
        if (realEstates == null) {
            realEstates = new ArrayList<>();
        }
        realEstates.add(realEstate);
        category.setEstates(realEstates);
        List<RealEstate> realEstate02 = category.getEstates();
        if (realEstate02 == null) {
            realEstate02 = new ArrayList<>();
        }
        realEstate02.add(realEstate);
        category.setEstates(realEstate02);
        List<Resource> resources = new ArrayList<>();
        // ResourceDTO => Resource
        for (ResourceDTO resourceDTO : realEstateDTO.getResources()) {
            Resource resource = new Resource();
            resource.setResourceType(resourceDTO.getResourceType());
            resource.setUrl(resourceDTO.getUrl());
            resource.setRealEstate(realEstate);
            resources.add(resource);
        }
        realEstate.setResource(resources);

        return realEstateRepository.save(realEstate);
    }


    public List<RealEstateDTO> getAllRealEstate() {
        List<RealEstate> estateList = realEstateRepository.findAll();
        List<RealEstateDTO> estateDTOList = new ArrayList<>();

        for (RealEstate realEstate : estateList) {
            RealEstateDTO realEstateDTO = new RealEstateDTO();
            //Map
            realEstateDTO.setId(realEstate.getId());
            realEstateDTO.setTitle(realEstate.getTitle());
            realEstateDTO.setDescription(realEstate.getDescription());
            realEstateDTO.setDate(realEstate.getDate());
            realEstateDTO.setAmount(realEstate.getAmount());
            if (realEstate.getCategory() != null)realEstateDTO.setCategoryId(realEstate.getCategory().getId());

            if (realEstate.getLocation() != null) realEstateDTO.setLocationId(realEstate.getLocation().getId());

            estateDTOList.add(realEstateDTO);

            List<ResourceDTO> resourceDTOS = new ArrayList<>();
            // ResourceDTO => Resource
            for (Resource resource : realEstate.getResource()) {
                ResourceDTO resourceDTO = new ResourceDTO();
                resourceDTO.setResourceType(resource.getResourceType());
                resourceDTO.setUrl(resource.getUrl());
                resourceDTOS.add(resourceDTO);
            }
            realEstateDTO.setResources(resourceDTOS);

        }
        return estateDTOList;
    }

}