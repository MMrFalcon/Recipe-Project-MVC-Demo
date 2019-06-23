package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.IngredientCommand;
import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.commands.UnitOfMeasureCommand;
import falcon.mvc.recipes.converters.IngredientToIngredientCommand;
import falcon.mvc.recipes.repositories.IngredientRepository;
import falcon.mvc.recipes.services.RecipeService;
import falcon.mvc.recipes.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    private IngredientServiceImpl ingredientService;
    private IngredientCommand ingredientCommand;
    private IngredientCommand secondIngredientCommand;
    private RecipeCommand recipeCommand;
    private UnitOfMeasureCommand unitOfMeasureCommand;
    private UnitOfMeasureCommand secondUnitOfMeasureCommand;

    private final static Long INGREDIENT_ID = 1L;
    private final static Long SECOND_INGREDIENT_ID = 2L;
    private final static Long NEW_INGREDIENT_ID = 13L;
    private final static String INGREDIENT_NAME = "Salt";
    private final static String NEW_INGREDIENT_NAME = "Pepper";
    private final static Long RECIPE_ID = 1L;
    private final static Long UNIT_OF_MEASURE_ID = 1L;
    private final static Long SECOND_UNIT_OF_MEASURE_ID = 2L;
    private final static String UNIT_OF_MEASURE_UNIT = "each";
    private final static BigDecimal INGREDIENT_AMOUNT = new BigDecimal(12);

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientToIngredientCommand ingredientToIngredientCommand;

    @Mock
    private RecipeService recipeService;

    @Mock
    private UnitOfMeasureService unitOfMeasureService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientService = new IngredientServiceImpl(ingredientRepository, ingredientToIngredientCommand,
                recipeService, unitOfMeasureService);

        unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(UNIT_OF_MEASURE_ID);
        unitOfMeasureCommand.setUnit(UNIT_OF_MEASURE_UNIT);

        secondUnitOfMeasureCommand = new UnitOfMeasureCommand();
        secondUnitOfMeasureCommand.setId(SECOND_UNIT_OF_MEASURE_ID);
        secondUnitOfMeasureCommand.setUnit(UNIT_OF_MEASURE_UNIT);

        ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setName(INGREDIENT_NAME);
        ingredientCommand.setRecipeId(RECIPE_ID);
        ingredientCommand.setUnitOfMeasure(unitOfMeasureCommand);
        ingredientCommand.setAmount(INGREDIENT_AMOUNT);

        secondIngredientCommand = new IngredientCommand();
        secondIngredientCommand.setId(SECOND_INGREDIENT_ID);
        secondIngredientCommand.setName(INGREDIENT_NAME);
        secondIngredientCommand.setAmount(INGREDIENT_AMOUNT);
        secondIngredientCommand.setUnitOfMeasure(secondUnitOfMeasureCommand);

        recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.getIngredients().add(ingredientCommand);
        recipeCommand.getIngredients().add(secondIngredientCommand);

    }

    @Test
    public void getIngredientByRecipeIdAndIngredientId() {
        when(recipeService.getRecipeCommandById(RECIPE_ID)).thenReturn(recipeCommand);

        IngredientCommand foundIngredient =
                ingredientService.getIngredientByRecipeIdAndIngredientId(RECIPE_ID, SECOND_INGREDIENT_ID);

        assertNotNull(foundIngredient);
        assertEquals(SECOND_INGREDIENT_ID, foundIngredient.getId());
    }

    @Test
    public void createOrUpdateIngredientCommand_Update() {

        when(recipeService.getRecipeCommandById(RECIPE_ID)).thenReturn(recipeCommand);
        when(recipeService.saveRecipeCommand(recipeCommand)).thenReturn(recipeCommand);
        when(unitOfMeasureService.getUnitOfMeasureById(UNIT_OF_MEASURE_ID)).thenReturn(unitOfMeasureCommand);

        IngredientCommand updatedIngredient = ingredientService.createOrUpdateIngredientCommand(ingredientCommand);

        assertNotNull(updatedIngredient);
        assertEquals(RECIPE_ID, updatedIngredient.getRecipeId());
        verify(recipeService, times(1)).getRecipeCommandById(RECIPE_ID);
        verify(recipeService, times(1)).saveRecipeCommand(recipeCommand);
        verify(unitOfMeasureService, times(1)).getUnitOfMeasureById(UNIT_OF_MEASURE_ID);
    }

    @Test
    public void createOrUpdateIngredientCommand_Save() {

        IngredientCommand newIngredient = new IngredientCommand();
        newIngredient.setId(NEW_INGREDIENT_ID);
        newIngredient.setName(NEW_INGREDIENT_NAME);
        newIngredient.setAmount(new BigDecimal(2));
        newIngredient.setUnitOfMeasure(unitOfMeasureCommand);
        newIngredient.setRecipeId(recipeCommand.getId());

        when(recipeService.getRecipeCommandById(RECIPE_ID)).thenReturn(recipeCommand);
        when(recipeService.saveRecipeCommand(recipeCommand)).thenReturn(recipeCommand);

        IngredientCommand savedIngredient = ingredientService.createOrUpdateIngredientCommand(newIngredient);

        assertNotNull(savedIngredient);
        assertEquals(RECIPE_ID, savedIngredient.getRecipeId());
        assertEquals(NEW_INGREDIENT_NAME, savedIngredient.getName());
        verify(recipeService, times(1)).saveRecipeCommand(recipeCommand);
        verify(recipeService, times(1)).getRecipeCommandById(RECIPE_ID);
    }

    @Test
    public void deleteIngredientById() {

        ingredientService.deleteIngredientById(INGREDIENT_ID);
        verify(ingredientRepository, times(1)).deleteById(INGREDIENT_ID);
    }
}