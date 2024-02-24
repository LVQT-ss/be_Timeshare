package tech.rent.be.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PostRequestDTO {

    Long id;
    String content;
    String title;
    Long Price;
    Date PostDate;
    Long userId;
}