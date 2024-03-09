package tech.rent.be.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

import java.sql.Time;
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

    Long price;

    Time checkIn;
    Time checkOut;



    @ManyToOne
    @JoinColumn(name = "user_id")
            @JsonIgnore
    Users users;

    @JsonIgnore
    @OneToMany(mappedBy = "realEstate")
    List<Booking> booking;

    @OneToMany(mappedBy = "realEstate", cascade = CascadeType.ALL)
    List<Resource>resource;

    @ManyToOne
    @JoinColumn(name = "category")
    @JsonIgnore
    Category category;

    @ManyToOne
    @JoinColumn(name = "location")
    @JsonIgnore
    Location location;


    @OneToMany(mappedBy = "realEstate")
    @JsonIgnore
    List<Post> post;



}