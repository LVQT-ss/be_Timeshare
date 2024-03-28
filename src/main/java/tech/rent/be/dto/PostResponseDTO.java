package tech.rent.be.dto;

import lombok.Data;
import tech.rent.be.entity.RealEstate;
import tech.rent.be.entity.Users;
import tech.rent.be.enums.PostStatus;

import java.sql.Time;
import java.util.Date;
import java.util.List;
@Data
public class PostResponseDTO {
    Long id;
    Long discount;
    Date FromDay;
    Date ToDay;
    RealEstateDTO realEstate;
    Users users;
    String thumbnail;
    PostStatus postStatus;
}
