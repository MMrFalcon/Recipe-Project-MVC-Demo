package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.converters.RecipeCommandToRecipe;
import falcon.mvc.recipes.converters.RecipeToRecipeCommand;
import falcon.mvc.recipes.domains.Recipe;
import falcon.mvc.recipes.repositories.RecipeRepository;
import falcon.mvc.recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeCommandToRecipe recipeCommandToRecipe,
                             RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Transactional
    @Override
    public List<RecipeCommand> getAllRecipes() {
        log.debug("Loading Set of Recipes");
        List<RecipeCommand> recipes = new ArrayList<>();
        for (Recipe recipe : recipeRepository.findAll()) {
            recipes.add(recipeToRecipeCommand.convert(recipe));
        }
        return recipes;
    }


    @Transactional
    @Override
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved Recipe Id:" + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Transactional
    @Override
    public RecipeCommand getRecipeCommandById(Long recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        log.debug("Searching for recipe with id: " + recipeId);
        if (!recipe.isPresent())
            throw new RuntimeException("Recipe not found");
        log.debug("Recipe was found: " + recipe.get());
        return recipeToRecipeCommand.convert(recipe.get());
    }

    @Override
    public void deleteById(Long recipeId) {
        log.debug("Deleting recipe with ID " + recipeId);
        recipeRepository.deleteById(recipeId);
    }

}
