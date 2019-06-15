package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.converters.RecipeCommandToRecipe;
import falcon.mvc.recipes.converters.RecipeToRecipeCommand;
import falcon.mvc.recipes.domains.Recipe;
import falcon.mvc.recipes.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {
    private RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    public void getRecipeById() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        Recipe recipeReturned = recipeService.getById(1L);

        assertNotNull("Null recipe returned", recipeReturned);
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void getAllRecipes() {

        Recipe recipe = new Recipe();
        Set<Recipe> recipesData = new HashSet<>();
        recipesData.add(recipe);

        when(recipeService.getAllRecipes()).thenReturn(recipesData);

        Set<Recipe> recipes = recipeService.getAllRecipes();

        assertEquals(recipes.size(), 1);
        verify(recipeRepository, times(1)).findAll();
        verify(recipeRepository, never()).findById(anyLong());
    }

    @Test
    public void createRecipes() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Recipe secondRecipe = new Recipe();
        secondRecipe.setId(2L);
        Set<Recipe> recipesData = new HashSet<>();
        recipesData.add(recipe);
        recipesData.add(secondRecipe);

        when(recipeRepository.saveAll(recipesData)).thenReturn(recipesData);
        List<RecipeCommand> savedRecipes = recipeService.createRecipes(recipesData);

        assertNotNull(savedRecipes);
        assertEquals(recipesData.size(), savedRecipes.size());
    }

    @Test
    public void saveRecipeCommand() {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        recipeCommand.setDescription("Desc");

        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeRepository.save(any())).thenReturn(recipe);
        when(recipeToRecipeCommand.convert(recipe)).thenReturn(recipeCommand);
        RecipeCommand savedRecipe = recipeService.saveRecipeCommand(recipeCommand);

        assertEquals(savedRecipe.getId(), recipeCommand.getId());
        verify(recipeRepository, times(1)).save(any());
        verify(recipeCommandToRecipe, times(1)).convert(recipeCommand);
        verify(recipeToRecipeCommand, times(1)).convert(recipe);
    }

}