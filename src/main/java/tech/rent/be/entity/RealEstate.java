package tech.rent.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class RealEstate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;
    String title;
    String description;
    Date date;
    Long amount;



    @ManyToOne
    @JoinColumn(name = "user_id")
    Users users;
    @OneToOne(mappedBy = "realEstate")
    Booking booking;
    @OneToMany(mappedBy = "realEstate", cascade = CascadeType.ALL)
    List<Resource>resource;

    @ManyToOne
    @JoinColumn(name = "category")
    Category category;

}