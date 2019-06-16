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

import java.util.*;

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
    public void getRecipeCommandById() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand recipeReturned = recipeService.getRecipeCommandById(1L);

        assertNotNull(recipeReturned);
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void getAllRecipes() {

        RecipeCommand recipe = new RecipeCommand();
        List<RecipeCommand> recipesData = new ArrayList<>();
        recipesData.add(recipe);

        Set<Recipe> recipesFromRepository = new HashSet<>();
        recipesFromRepository.add(new Recipe());

        when(recipeRepository.findAll()).thenReturn(recipesFromRepository);
        when(recipeToRecipeCommand.convert(any())).thenReturn(recipe);

        List<RecipeCommand> recipes = recipeService.getAllRecipes();

        assertEquals(recipes.size(), 1);
        verify(recipeRepository, times(1)).findAll();
        verify(recipeRepository, never()).findById(anyLong());
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