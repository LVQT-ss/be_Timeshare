package tech.rent.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.rent.be.entity.RealEstate;

import java.util.List;

public interface RealEstateRepository  extends JpaRepository<RealEstate,Long> {
    RealEstate findRealEstateById(long id);
}
