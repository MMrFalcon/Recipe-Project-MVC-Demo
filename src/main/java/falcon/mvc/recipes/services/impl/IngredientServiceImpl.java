package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.IngredientCommand;
import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.converters.IngredientToIngredientCommand;
import falcon.mvc.recipes.domains.Ingredient;
import falcon.mvc.recipes.repositories.IngredientRepository;
import falcon.mvc.recipes.services.IngredientService;
import falcon.mvc.recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final RecipeService recipeService;

    public IngredientServiceImpl(IngredientRepository ingredientRepository,
                                 IngredientToIngredientCommand ingredientToIngredientCommand, RecipeService recipeService) {

        this.ingredientRepository = ingredientRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeService = recipeService;
    }

    @Override
    public IngredientCommand getIngredientByName(String name) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByName(name);
        if (optionalIngredient.isPresent()) {
            log.debug("Searching for Ingredient with name " + name);
            return ingredientToIngredientCommand.convert(optionalIngredient.get());
        }else {
            throw new RuntimeException("No such Ingredient!");
        }
    }

    @Override
    public IngredientCommand getIngredientByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

        RecipeCommand recipeCommand = recipeService.getRecipeCommandById(recipeId);

        Optional<IngredientCommand> ingredientCommand = recipeCommand.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId)).findFirst();

        if (!ingredientCommand.isPresent())
            throw new RuntimeException("Ingredient not found");

        return ingredientCommand.get();
    }
}
