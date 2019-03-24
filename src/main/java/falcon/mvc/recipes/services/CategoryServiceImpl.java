package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.Category;
import falcon.mvc.recipes.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

   private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Category getByDescription(String description) {
        if (categoryRepository.findByDescription(description).isPresent()) {
            return categoryRepository.findByDescription(description).get();
        }else {
            throw new RuntimeException("No such category");
        }
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
}
