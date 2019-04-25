package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.Recipe;

import java.util.List;
import java.util.Set;

public interface RecipeService {
    Set<Recipe> getAllRecipes();
    Recipe createRecipe(Recipe recipe);
    Iterable<Recipe> createRecipes(List<Recipe> recipes);
}
