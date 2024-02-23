package tech.rent.be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.rent.be.dto.CategoryDTO;
import tech.rent.be.entity.Category;
import tech.rent.be.repository.CategoryRepository;
import tech.rent.be.repository.RealEstateRepository;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;


    public Category CreateCategory(CategoryDTO categoryDTO){
        Category category = new Category();
        category.setCategoryname(categoryDTO.getCategoryname());
        return categoryRepository.save(category);
    }

}
