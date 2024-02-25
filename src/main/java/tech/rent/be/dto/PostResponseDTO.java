package tech.rent.be.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class PostResponseDTO {
    Long id;
    String content;
    String title;
    Long Price;
    Date PostDate;
    Long userId;
    List<ResourceDTO> resources;
}
