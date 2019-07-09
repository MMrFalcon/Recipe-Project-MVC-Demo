package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.IngredientCommand;
import falcon.mvc.recipes.commands.UnitOfMeasureCommand;
import falcon.mvc.recipes.domains.Ingredient;
import falcon.mvc.recipes.domains.Notes;
import falcon.mvc.recipes.domains.Recipe;
import falcon.mvc.recipes.domains.UnitOfMeasure;
import falcon.mvc.recipes.exceptions.NotFoundException;
import falcon.mvc.recipes.repositories.IngredientRepository;
import falcon.mvc.recipes.repositories.RecipeRepository;
import falcon.mvc.recipes.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IngredientServiceTestIT {

    private static final String INGREDIENT_NAME = "pepper";
    private static final String SECOND_INGREDIENT_NAME = "salt";
    private static final Long NON_EXISTING_INGREDIENT_ID = 123456L;
    private static final Long IMPORTED_UOM_ID = 1L;
    private static final BigDecimal INGREDIENT_AMOUNT = new BigDecimal(16);
    private static final Long NEW_INGREDIENT_ID = 12L;

    private Ingredient ingredientForRecipe;

    private Recipe recipe;

    private Recipe savedRecipe;

    private UnitOfMeasureCommand unitOfMeasureCommand;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Before
    public void setUp() throws Exception {

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUnit("pinch");

        UnitOfMeasure savedUnitOfMeasure = unitOfMeasureRepository.save(unitOfMeasure);

        ingredientForRecipe = new Ingredient(SECOND_INGREDIENT_NAME, new BigDecimal(2), savedUnitOfMeasure);

        recipe = new Recipe();
        recipe.addIngredient(ingredientForRecipe);
        recipe.setNotes(new Notes());
        savedRecipe = recipeRepository.save(recipe);

        unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(IMPORTED_UOM_ID);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getIngredientByRecipeIdAndIngredientId() {
        IngredientCommand foundIngredient =
                ingredientService.getIngredientByRecipeIdAndIngredientId(savedRecipe.getId(), ingredientForRecipe.getId());

        assertNotNull(foundIngredient);
        assertEquals(SECOND_INGREDIENT_NAME, foundIngredient.getName());
    }

    @Test
    public void getIngredientByRecipeIdAndIngredientIdNotPresent() {
        final String exceptionMessage = "Ingredient not found";
        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage(exceptionMessage);
        ingredientService.getIngredientByRecipeIdAndIngredientId(savedRecipe.getId(), NON_EXISTING_INGREDIENT_ID);
    }

    @Test
    public void createOrUpdateIngredientCommand_Update() {
            IngredientCommand ingredientUpdate = new IngredientCommand();
            ingredientUpdate.setId(ingredientForRecipe.getId());
            ingredientUpdate.setName(INGREDIENT_NAME);
            ingredientUpdate.setUnitOfMeasure(unitOfMeasureCommand);
            ingredientUpdate.setRecipeId(savedRecipe.getId());
            ingredientUpdate.setAmount(INGREDIENT_AMOUNT);

            IngredientCommand updatedIngredient = ingredientService.createOrUpdateIngredientCommand(ingredientUpdate);
            assertNotNull(updatedIngredient);
            assertEquals(savedRecipe.getId(), updatedIngredient.getRecipeId());
            assertEquals(INGREDIENT_NAME, updatedIngredient.getName());
    }

    @Test
    public void createOrUpdateIngredientCommand_Save() {
        IngredientCommand ingredientForSave = new IngredientCommand();
        ingredientForSave.setId(NEW_INGREDIENT_ID);
        ingredientForSave.setName(SECOND_INGREDIENT_NAME);
        ingredientForSave.setUnitOfMeasure(unitOfMeasureCommand);
        ingredientForSave.setRecipeId(savedRecipe.getId());
        ingredientForSave.setAmount(INGREDIENT_AMOUNT);

        IngredientCommand savedIngredient = ingredientService.createOrUpdateIngredientCommand(ingredientForSave);
        assertNotNull(savedIngredient);
        assertEquals(SECOND_INGREDIENT_NAME, savedIngredient.getName());
        assertEquals(savedRecipe.getId(), savedIngredient.getRecipeId());
    }

    @Test
    public void deleteIngredientById() {
        Ingredient ingredientForDelete = new Ingredient();
        ingredientForDelete.setUnitOfMeasure(unitOfMeasureRepository.findById(IMPORTED_UOM_ID).get());
        ingredientForDelete.setName(SECOND_INGREDIENT_NAME);

        Ingredient savedIngredient = ingredientRepository.save(ingredientForDelete);
        ingredientService.deleteIngredientById(savedIngredient.getId());

        assertFalse(ingredientRepository.findById(savedIngredient.getId()).isPresent());
    }
}