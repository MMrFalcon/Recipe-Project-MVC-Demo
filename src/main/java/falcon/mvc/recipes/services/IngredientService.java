package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.Ingredient;
import falcon.mvc.recipes.repositories.IngredientRepository;

public interface IngredientService extends BaseService<Ingredient, Long, IngredientRepository> {
    Ingredient getByName(String name);
}
