package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.CategoryCommand;
import falcon.mvc.recipes.converters.CategoryCommandToCategory;
import falcon.mvc.recipes.converters.CategoryToCategoryCommand;
import falcon.mvc.recipes.domains.Category;
import falcon.mvc.recipes.repositories.CategoryRepository;
import falcon.mvc.recipes.services.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

   private final CategoryRepository categoryRepository;
   private final CategoryToCategoryCommand categoryToCategoryCommand;
   private final CategoryCommandToCategory categoryCommandToCategory;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryToCategoryCommand categoryToCategoryCommand,
                               CategoryCommandToCategory categoryCommandToCategory) {

        this.categoryToCategoryCommand = categoryToCategoryCommand;
        this.categoryRepository = categoryRepository;
        this.categoryCommandToCategory = categoryCommandToCategory;
    }


    @Override
    public CategoryCommand getCategoryByDescription(String description) {
        if (categoryRepository.findByDescription(description).isPresent()) {
            log.debug("Searching for category...");
            return categoryToCategoryCommand.convert(categoryRepository.findByDescription(description).get());
        }else {
            throw new RuntimeException("No such category");
        }
    }

    @Override
    public CategoryCommand createCategory(CategoryCommand category) {
        log.debug("Saving category " + category);
        Category savedCategory = categoryRepository.save(categoryCommandToCategory.convert(category));
        return categoryToCategoryCommand.convert(savedCategory);
    }
}
