package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.RecipeCommand;

import java.util.List;

public interface RecipeService {
    List<RecipeCommand> getAllRecipes();
    RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand);
    RecipeCommand getRecipeCommandById(Long recipeId);
    void deleteById(Long id);
}
