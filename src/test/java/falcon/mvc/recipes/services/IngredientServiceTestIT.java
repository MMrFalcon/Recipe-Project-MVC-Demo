package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.IngredientCommand;
import falcon.mvc.recipes.commands.UnitOfMeasureCommand;
import falcon.mvc.recipes.domains.Ingredient;
import falcon.mvc.recipes.domains.Notes;
import falcon.mvc.recipes.domains.Recipe;
import falcon.mvc.recipes.domains.UnitOfMeasure;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IngredientServiceTestIT {

    private static final String INGREDIENT_NAME = "pepper";
    private static final String SECOND_INGREDIENT_NAME = "salt";
    private static final Long NON_EXISTING_INGREDIENT_ID = 123456L;
    private static final Long IMPORTED_UOM_ID = 1L;

    private Ingredient ingredient;

    private Ingredient ingredientForRecipe;

    private Recipe recipe;

    private Recipe savedRecipe;

    private UnitOfMeasureCommand unitOfMeasureCommand;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUnit("pinch");

        UnitOfMeasure savedUnitOfMeasure = unitOfMeasureRepository.save(unitOfMeasure);

        ingredient = new Ingredient();
        ingredient.setName(INGREDIENT_NAME);
        ingredient.setUnitOfMeasure(savedUnitOfMeasure);

        ingredientRepository.save(ingredient);

        ingredientForRecipe = new Ingredient(SECOND_INGREDIENT_NAME, new BigDecimal(2), savedUnitOfMeasure);

        recipe = new Recipe();
        recipe.addIngredient(ingredientForRecipe);
        recipe.setNotes(new Notes());
        savedRecipe = recipeRepository.save(recipe);

        unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(IMPORTED_UOM_ID);
    }

    @Test
    public void getIngredientByName() {
        IngredientCommand ingredientCommand = ingredientService.getIngredientByName(INGREDIENT_NAME);

        assertNotNull(ingredientCommand);
        assertEquals(ingredientCommand.getName(), ingredient.getName());
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getIngredientByNameNotPresent() {
        final String exceptionMessage = "No such Ingredient!";
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage(exceptionMessage);
        ingredientService.getIngredientByName("Does not exist");
    }

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
        exceptionRule.expect(RuntimeException.class);
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

            IngredientCommand updatedIngredient = ingredientService.createOrUpdateIngredientCommand(ingredientUpdate);
            assertNotNull(updatedIngredient);
            assertEquals(savedRecipe.getId(), updatedIngredient.getRecipeId());
            assertEquals(INGREDIENT_NAME, updatedIngredient.getName());
    }
    //TODO save ingredient tests
}