package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.IngredientCommand;
import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.commands.UnitOfMeasureCommand;
import falcon.mvc.recipes.converters.IngredientToIngredientCommand;
import falcon.mvc.recipes.domains.Ingredient;
import falcon.mvc.recipes.repositories.IngredientRepository;
import falcon.mvc.recipes.services.RecipeService;
import falcon.mvc.recipes.services.UnitOfMeasureService;
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
    private UnitOfMeasureCommand unitOfMeasureCommand;

    private final static Long INGREDIENT_ID = 1L;
    private final static Long SECOND_INGREDIENT_ID = 2L;
    private final static Long NEW_INGREDIENT_ID = 12L;
    private final static String INGREDIENT_NAME = "Salt";
    private final static Long RECIPE_ID = 1L;
    private final static Long UNIT_OF_MEASURE_ID = 1L;
    private final static String UNIT_OF_MEASURE_UNIT = "each";

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

        ingredientFromRepository = new Ingredient();
        ingredientFromRepository.setId(INGREDIENT_ID);
        ingredientFromRepository.setName(INGREDIENT_NAME);

        unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(UNIT_OF_MEASURE_ID);
        unitOfMeasureCommand.setUnit(UNIT_OF_MEASURE_UNIT);

        ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(INGREDIENT_ID);
        ingredientCommand.setName(INGREDIENT_NAME);
        ingredientCommand.setRecipeId(RECIPE_ID);
        ingredientCommand.setUnitOfMeasure(unitOfMeasureCommand);

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

    //TODO save ingredient tests after watching video
//    @Test
//    public void createOrUpdateIngredientCommand_Save() {
//
//        IngredientCommand newIngredient = new IngredientCommand();
//        newIngredient.setId(NEW_INGREDIENT_ID);
//        newIngredient.setUnitOfMeasure(unitOfMeasureCommand);
//
//        when(recipeService.getRecipeCommandById(RECIPE_ID)).thenReturn(recipeCommand);
//        when(recipeService.saveRecipeCommand(recipeCommand)).thenReturn(recipeCommand);
//
//        IngredientCommand savedIngredient = ingredientService.createOrUpdateIngredientCommand(newIngredient);
//
//        assertNotNull(savedIngredient);
//        assertEquals(RECIPE_ID, savedIngredient.getRecipeId());
//    }
}