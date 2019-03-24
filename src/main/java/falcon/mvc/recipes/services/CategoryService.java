package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.Category;
import falcon.mvc.recipes.repositories.CategoryRepository;

public interface CategoryService extends BaseService<Category, Long, CategoryRepository> {

    Category getByDescription(String description);
    Category createCategory(Category category);

}
