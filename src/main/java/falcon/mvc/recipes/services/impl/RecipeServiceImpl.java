package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.domains.Recipe;
import falcon.mvc.recipes.repositories.RecipeRepository;
import falcon.mvc.recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Set<Recipe> getAllRecipes() {
        log.debug("Loading Set of Recipes");
        Set<Recipe> recipes = new HashSet<>();
        for (Recipe recipe : recipeRepository.findAll()) recipes.add(recipe);
        return recipes;
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {
        log.debug("Saving recipe " + recipe);
        return recipeRepository.save(recipe);
    }

    @Override
    public Iterable<Recipe> createRecipes(List<Recipe> recipes) {
        log.debug("Saving list of Recipes " + recipes);
        return recipeRepository.saveAll(recipes);
    }

}
