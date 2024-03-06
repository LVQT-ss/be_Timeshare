package tech.rent.be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;
    Date checkOut;
    Date checkIn;
    Date bookingDate;
    Boolean Status;
    int amount;
    @ManyToOne
    @JoinColumn(name = "user_id")
    Users users;

    @OneToOne(mappedBy = "booking")
    Payment payment;
    @ManyToOne
    @JoinColumn(name = "estate_id", unique = false)
    RealEstate realEstate;
}
