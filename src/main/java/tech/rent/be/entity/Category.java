package tech.rent.be.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Category {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;
    String Categoryname;

    @OneToMany(mappedBy = "category")
    List<RealEstate> estates;

}
