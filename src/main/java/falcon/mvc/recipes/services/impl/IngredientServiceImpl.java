package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.IngredientCommand;
import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.converters.IngredientToIngredientCommand;
import falcon.mvc.recipes.domains.Ingredient;
import falcon.mvc.recipes.repositories.IngredientRepository;
import falcon.mvc.recipes.services.IngredientService;
import falcon.mvc.recipes.services.RecipeService;
import falcon.mvc.recipes.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    private final RecipeService recipeService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientServiceImpl(IngredientRepository ingredientRepository,
                                 IngredientToIngredientCommand ingredientToIngredientCommand,
                                 RecipeService recipeService, UnitOfMeasureService unitOfMeasureService) {

        this.ingredientRepository = ingredientRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeService = recipeService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @Override
    public IngredientCommand getIngredientByName(String name) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByName(name);
        if (optionalIngredient.isPresent()) {
            log.debug("Searching for Ingredient with name " + name);
            return ingredientToIngredientCommand.convert(optionalIngredient.get());
        } else {
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

    @Transactional
    @Override
    public IngredientCommand createOrUpdateIngredientCommand(IngredientCommand ingredientCommand) {
        log.debug("Starting creation or update operation on ingredient command " + ingredientCommand);
        RecipeCommand recipeCommand = recipeService.getRecipeCommandById(ingredientCommand.getRecipeId());

        Optional<IngredientCommand> ingredientCommandOptional = recipeCommand.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                .findFirst();

        if (ingredientCommandOptional.isPresent()) {
            log.debug("Updating ingredient");
            updateIngredient(ingredientCommandOptional.get(), ingredientCommand);
        } else {
            log.debug("Adding new ingredient");
            recipeCommand.getIngredients().add(ingredientCommand);
        }

        RecipeCommand savedRecipe = recipeService.saveRecipeCommand(recipeCommand);

        return savedRecipe.getIngredients().stream()
                .filter(recipeIngredient -> recipeIngredient.getId().equals(ingredientCommand.getId()))
                .findFirst().orElseThrow(() -> new RuntimeException("Ingredient was not properly saved"));
    }

    private void updateIngredient(IngredientCommand existingIngredient, IngredientCommand updatedIngredientCommand) {
        existingIngredient.setName(updatedIngredientCommand.getName());
        existingIngredient.setAmount(updatedIngredientCommand.getAmount());
        existingIngredient.setUnitOfMeasure(
                unitOfMeasureService.getUnitOfMeasureById(updatedIngredientCommand.getUnitOfMeasure().getId()));
    }
}
