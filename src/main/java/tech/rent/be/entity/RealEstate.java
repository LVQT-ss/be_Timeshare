package tech.rent.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RealEstate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String description;
    Date date;
    Long amount;



    @ManyToOne
    @JoinColumn(name = "user_id")
    Users users;

    @JsonIgnore
    @OneToMany(mappedBy = "realEstate")
    List<Booking> booking;

    @JsonIgnore
    @OneToMany(mappedBy = "realEstate", cascade = CascadeType.ALL)
    List<Resource>resource;

    @ManyToOne
    @JoinColumn(name = "category")
    Category category;

    @ManyToOne
    @JoinColumn(name = "location")
    Location location;




}