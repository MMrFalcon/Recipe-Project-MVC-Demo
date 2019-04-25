package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.Ingredient;

public interface IngredientService {
    Ingredient getByName(String name);
}
