package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.IngredientCommand;
import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.converters.IngredientToIngredientCommand;
import falcon.mvc.recipes.domains.Ingredient;
import falcon.mvc.recipes.repositories.IngredientRepository;
import falcon.mvc.recipes.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    private IngredientServiceImpl ingredientService;
    private Ingredient ingredientFromRepository;
    private IngredientCommand ingredientCommand;
    private IngredientCommand secondIngredientCommand;
    private RecipeCommand recipeCommand;

    private final static Long INGREDIENT_ID = 1L;
    private final static Long SECOND_INGREDIENT_ID = 2L;
    private final static String INGREDIENT_NAME = "Salt";
    private final static Long RECIPE_ID = 1L;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientToIngredientCommand ingredientToIngredientCommand;

    @Mock
    private RecipeService recipeService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientService = new IngredientServiceImpl(ingredientRepository, ingredientToIngredientCommand, recipeService);

        ingredientFromRepository = new Ingredient();
        ingredientFromRepository.setId(INGREDIENT_ID);
        ingredientFromRepository.setName(INGREDIENT_NAME);

        ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setName(INGREDIENT_NAME);

        secondIngredientCommand = new IngredientCommand();
        secondIngredientCommand.setId(SECOND_INGREDIENT_ID);
        secondIngredientCommand.setName(INGREDIENT_NAME);

        recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.getIngredients().add(ingredientCommand);
        recipeCommand.getIngredients().add(secondIngredientCommand);
    }

    @Test
    public void getIngredientByName() {
        Optional<Ingredient> optionalIngredient = Optional.of(ingredientFromRepository);

        when(ingredientRepository.findByName(INGREDIENT_NAME)).thenReturn(optionalIngredient);
        when(ingredientToIngredientCommand.convert(any())).thenReturn(ingredientCommand);

        IngredientCommand foundIngredient = ingredientService.getIngredientByName(INGREDIENT_NAME);

        assertNotNull(foundIngredient);
        verify(ingredientRepository, times(1)).findByName(INGREDIENT_NAME);
        verify(ingredientToIngredientCommand, times(1)).convert(any());
    }

    @Test
    public void getIngredientByRecipeIdAndIngredientId() {
        when(recipeService.getRecipeCommandById(RECIPE_ID)).thenReturn(recipeCommand);

        IngredientCommand foundIngredient =
                ingredientService.getIngredientByRecipeIdAndIngredientId(RECIPE_ID, SECOND_INGREDIENT_ID);

        assertNotNull(foundIngredient);
        assertEquals(SECOND_INGREDIENT_ID, foundIngredient.getId());
    }
}