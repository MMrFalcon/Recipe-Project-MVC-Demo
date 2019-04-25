package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.domains.Category;
import falcon.mvc.recipes.repositories.CategoryRepository;
import falcon.mvc.recipes.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

   private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Category getByDescription(String description) {
        if (categoryRepository.findByDescription(description).isPresent()) {
            log.debug("Searching for category...");
            return categoryRepository.findByDescription(description).get();
        }else {
            throw new RuntimeException("No such category");
        }
    }

    @Override
    public Category createCategory(Category category) {
        log.debug("Saving category " + category);
        return categoryRepository.save(category);
    }
}
