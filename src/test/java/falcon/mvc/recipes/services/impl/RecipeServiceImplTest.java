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
    private static final Long RECIPE_ID = 1L;
    private static final String RECIPE_DESCRIPTION = "Description";

    private Recipe recipeFromRepository;
    private RecipeCommand recipeCommand;

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

        recipeFromRepository = new Recipe();
        recipeFromRepository.setId(RECIPE_ID);
        recipeFromRepository.setDescription(RECIPE_DESCRIPTION);

        recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.setDescription(RECIPE_DESCRIPTION);
    }

    @Test
    public void getRecipeCommandById() {
        Optional<Recipe> recipeOptional = Optional.of(recipeFromRepository);


        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand recipeReturned = recipeService.getRecipeCommandById(1L);

        assertNotNull(recipeReturned);
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void getAllRecipes() {

        Set<Recipe> recipesFromRepository = new HashSet<>();
        recipesFromRepository.add(recipeFromRepository);

        when(recipeRepository.findAll()).thenReturn(recipesFromRepository);
        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        List<RecipeCommand> recipes = recipeService.getAllRecipes();

        assertEquals(recipes.size(), 1);
        verify(recipeRepository, times(1)).findAll();
        verify(recipeRepository, never()).findById(anyLong());
    }

    @Test
    public void saveRecipeCommand() {

        when(recipeRepository.save(any())).thenReturn(recipeFromRepository);
        when(recipeToRecipeCommand.convert(recipeFromRepository)).thenReturn(recipeCommand);
        RecipeCommand savedRecipe = recipeService.saveRecipeCommand(recipeCommand);

        assertEquals(savedRecipe.getId(), recipeCommand.getId());
        verify(recipeRepository, times(1)).save(any());
        verify(recipeCommandToRecipe, times(1)).convert(recipeCommand);
        verify(recipeToRecipeCommand, times(1)).convert(recipeFromRepository);
    }

}