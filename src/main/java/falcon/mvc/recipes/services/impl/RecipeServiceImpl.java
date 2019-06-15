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

import java.util.*;

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
    public Set<Recipe> getAllRecipes() {
        log.debug("Loading Set of Recipes");
        Set<Recipe> recipes = new HashSet<>();
        for (Recipe recipe : recipeRepository.findAll()) recipes.add(recipe);
        return recipes;
    }


    @Override
    public List<RecipeCommand> createRecipes(Set<Recipe> recipes) {
        log.debug("Saving set of Recipes " + recipes);
        List<RecipeCommand> savedRecipes = new ArrayList<>();
        for (Recipe recipe : recipeRepository.saveAll(recipes)) {
            savedRecipes.add(recipeToRecipeCommand.convert(recipe));
        }
        log.debug("Saved list size " + savedRecipes.size());
        return savedRecipes;
    }

    @Transactional
    @Override
    public RecipeCommand saveRecipeCommand(RecipeCommand recipeCommand) {
        Recipe detachedRecipe = recipeCommandToRecipe.convert(recipeCommand);
        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved Recipe Id:" + savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    public Recipe getById(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        log.debug("Searching for recipe with id: " + id);
        if (!recipe.isPresent())
            throw new RuntimeException("Recipe not found");
        log.debug("Recipe was found: " + recipe.get());
        return recipe.get();
    }

}
