package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.CategoryCommand;

public interface CategoryService {

    CategoryCommand getCategoryByDescription(String description);
    CategoryCommand createCategory(CategoryCommand category);

}
