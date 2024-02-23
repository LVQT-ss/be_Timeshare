package tech.rent.be.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.rent.be.dto.CategoryDTO;
import tech.rent.be.dto.PaymentDTO;
import tech.rent.be.entity.Category;
import tech.rent.be.entity.Payment;
import tech.rent.be.services.CategoryService;

@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @PostMapping("/category")
    public ResponseEntity category(@RequestBody CategoryDTO categoryDTO){
        Category category = categoryService.CreateCategory(categoryDTO);
        return  ResponseEntity.ok(category);
    }
}
