package tech.rent.be.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BookingRequestDTO {
    Long Id;
    Date date;
    long estateId;
    int amount;
}
