package tech.rent.be.dto;

import lombok.Data;
import tech.rent.be.entity.Users;
import tech.rent.be.enums.PostStatus;

import java.util.Date;

@Data
public class PostRequestDTO {

    Long discount;
    Date FromDay;
    Date ToDay;
    long estateId;
    String thumbnail;
    PostStatus postStatus;
}