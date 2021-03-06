package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand getIngredientByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
    IngredientCommand createOrUpdateIngredientCommand(IngredientCommand ingredientCommand);
    void deleteIngredientById(Long ingredientId);
}
