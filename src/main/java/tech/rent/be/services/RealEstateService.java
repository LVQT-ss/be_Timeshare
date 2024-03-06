package tech.rent.be.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import tech.rent.be.dto.PostResponseDTO;
import tech.rent.be.dto.RealEstateDTO;
import tech.rent.be.dto.ResourceDTO;
import tech.rent.be.entity.*;
//import tech.rent.be.entity.Resource;
import tech.rent.be.repository.*;
import tech.rent.be.utils.AccountUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    BookingRepository bookingRepository;

    @PersistenceContext
    EntityManager entityManager;
    public RealEstate finRealEstateById(long id){
        try {
            return realEstateRepository.findRealEstateById(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

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
        return convertToDTO(estateList);
    }

    public List<RealEstateDTO> convertToDTO(List<RealEstate> estateList) {
        List<RealEstateDTO> estateDTOList = new ArrayList<>();

        for (RealEstate realEstate : estateList) {
            RealEstateDTO realEstateDTO = new RealEstateDTO();
            //Map
            realEstateDTO.setId(realEstate.getId());
            realEstateDTO.setTitle(realEstate.getTitle());
            realEstateDTO.setDescription(realEstate.getDescription());
            realEstateDTO.setDate(realEstate.getDate());
            realEstateDTO.setAmount(realEstate.getAmount());
            realEstateDTO.setLocation(realEstate.getLocation().getLocation());
            realEstateDTO.setCategory(realEstate.getCategory().getCategoryname());
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
    public List<RealEstateDTO> search(long categoryId, long locationId, long amount, LocalDate from, LocalDate to) {
        List<RealEstate> finalList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RealEstate> criteriaQuery = criteriaBuilder.createQuery(RealEstate.class);
        Root<RealEstate> realEstateRoot = criteriaQuery.from(RealEstate.class);
        Predicate predicate = criteriaBuilder.conjunction();
        if(categoryId != 0){
            Join<RealEstate, Category> categoryJoin = realEstateRoot.join("category", JoinType.INNER);
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(categoryJoin.get("id"), categoryId));
        }

        if(locationId != 0){
            Join<RealEstate, Location> categoryJoin = realEstateRoot.join("location", JoinType.INNER);
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(categoryJoin.get("id"), locationId));
        }

        if(amount != 0){
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(realEstateRoot.get("amount"), amount));
        }
        criteriaQuery.where(predicate);
        List<RealEstate> realEstates = entityManager.createQuery(criteriaQuery).getResultList();
        finalList.addAll(realEstates);
        if(from != null && to != null){
            boolean check = false;
            for(RealEstate realEstate: realEstates){
                List<Booking> bookings = bookingRepository.findBookingsByRealEstate(realEstate);
                for (Booking booking : bookings){
                    if(checkIfBookingFromTo(booking, convertStringToDate(convertLocalDateToString(from)+" 14:01:00"), convertStringToDate(convertLocalDateToString(to)+" 12:00:00"))){
                        check = true;
                    }
                }
                if(!check) finalList.add(realEstate);
            }

        }


//        return convertToDTO(entityManager.createQuery(criteriaQuery).getResultList());
        return convertToDTO(finalList);
    }

    public boolean checkIfBookingFromTo(Booking booking, Date from, Date to){
        return booking.getCheckIn().before(to) && booking.getCheckOut().after(from);
    }

    public static Date convertStringToDate(String dateString) {
        // Specify the format of the input string
        String dateFormat = "dd/MM/yyyy HH:mm:ss";

        // Create a SimpleDateFormat object with the specified format
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        try {
            // Parse the string to a Date object and return it
            return sdf.parse(dateString);
        } catch (ParseException e) {
            // Print the exception, but don't handle it here; return null instead
            e.printStackTrace();
            return null;
        }
    }

    public String convertLocalDateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }




}