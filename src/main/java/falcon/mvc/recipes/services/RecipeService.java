package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.domains.Recipe;

import java.util.List;
import java.util.Set;

public interface RecipeService {
    Set<Recipe> getAllRecipes();
    List<RecipeCommand> createRecipes(Set<Recipe> recipes);
    RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);
    Recipe getById(Long id);
}
