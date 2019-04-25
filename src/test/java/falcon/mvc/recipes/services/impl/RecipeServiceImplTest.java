package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.domains.*;
import falcon.mvc.recipes.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    private RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository);
    }

    @Test
    public void getAllRecipes() {
        Recipe recipe = new Recipe();
        Set<Recipe> recipesData = new HashSet<>();
        recipesData.add(recipe);
        when(recipeRepository.findAll()).thenReturn(recipesData);

        Set<Recipe> recipes = recipeService.getAllRecipes();

        assertEquals(recipes.size(), 1);
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    public void createRecipe() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Salt");

        Notes notes = new Notes();
        notes.setDescription("Description");

        Category category = new Category();
        category.setDescription("Description fo category");

        Recipe recipe = new Recipe();
        recipe.getIngredients().add(ingredient);
        recipe.setNotes(notes);
        recipe.getCategories().add(category);
        recipe.setDifficulty(Difficulty.EASY);

        Recipe createdRecipe = recipeService.createRecipe(recipe);
        Recipe savedRecipe = recipeRepository.save(recipe);

        assertEquals(createdRecipe, savedRecipe);
        verify(recipeRepository,times(2)).save(recipe);

    }

    @Test
    public void createRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe());

        Recipe secondRecipe = new Recipe();
        secondRecipe.setDescription("Different than first");
        recipes.add(secondRecipe);

        Iterable<Recipe> createdRecipes = recipeService.createRecipes(recipes);
        Iterable<Recipe> savedRecipes = recipeRepository.saveAll(recipes);

        assertEquals(createdRecipes,savedRecipes);
        verify(recipeRepository,times(2)).saveAll(recipes);
    }
}