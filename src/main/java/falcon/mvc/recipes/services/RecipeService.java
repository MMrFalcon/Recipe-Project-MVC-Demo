package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.Recipe;
import falcon.mvc.recipes.repositories.RecipeRepository;

import java.util.List;

public interface RecipeService extends BaseService<Recipe, Long, RecipeRepository> {
    List<Recipe> getAllRecipes();
    Recipe createRecipe(Recipe recipe);
    Iterable<Recipe> createRecipes(List<Recipe> recipes);
}
