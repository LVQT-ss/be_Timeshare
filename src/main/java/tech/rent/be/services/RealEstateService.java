package tech.rent.be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import tech.rent.be.dto.RealEstateDTO;
import tech.rent.be.dto.ResourceDTO;
import tech.rent.be.entity.Category;
import tech.rent.be.entity.RealEstate;
//import tech.rent.be.entity.Resource;
import tech.rent.be.entity.Resource;
import tech.rent.be.entity.Users;
import tech.rent.be.repository.CategoryRepository;
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

        public RealEstate createEstate(RealEstateDTO realEstateDTO){
            Users users = accountUtils.getCurrentUser();
            Category category = categoryRepository.findCategoryById(realEstateDTO.getCategoryId());
            RealEstate realEstate = new RealEstate();
            realEstate.setDescription(realEstateDTO.getDescription());
            realEstate.setTitle(realEstateDTO.getTitle());
            realEstate.setDescription(realEstateDTO.getDescription());
            realEstate.setDate(realEstateDTO.getDate());
            realEstate.setAmount(realEstateDTO.getAmount());
            realEstate.setCategory(category);
            realEstate.setUsers(users);

            List<RealEstate> realEstates = category.getEstates();

            if(realEstates == null){
                realEstates = new ArrayList<>();
            }

            realEstates.add(realEstate);

            category.setEstates(realEstates);

            List<Resource> resources = new ArrayList<>();
            // ResourceDTO => Resource
            for(ResourceDTO resourceDTO: realEstateDTO.getResources()){
                Resource resource = new Resource();
                resource.setResourceType(resourceDTO.getResourceType());
                resource.setUrl(resourceDTO.getUrl());
                resource.setRealEstate(realEstate);
                resources.add(resource);
            }
            realEstate.setResource(resources);

            return realEstateRepository.save(realEstate);
        }
}