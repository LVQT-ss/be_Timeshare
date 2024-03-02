package tech.rent.be.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BookingRequestDTO {
    Long Id;
//    Date checkOut;
//    Date checkIn;
    Date date;
//    Long userId;
    Long estateId;
    int amount;
}
