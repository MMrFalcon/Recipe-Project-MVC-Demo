package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.Category;

public interface CategoryService {

    Category getByDescription(String description);
    Category createCategory(Category category);

}
