package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.IngredientCommand;
import falcon.mvc.recipes.domains.Ingredient;
import falcon.mvc.recipes.domains.UnitOfMeasure;
import falcon.mvc.recipes.repositories.IngredientRepository;
import falcon.mvc.recipes.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IngredientServiceTestIT {

    private static final String INGREDIENT_NAME = "pepper";

    private Ingredient ingredient;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Before
    public void setUp() throws Exception {

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setUnit("pinch");

        UnitOfMeasure savedUnitOfMeasure = unitOfMeasureRepository.save(unitOfMeasure);

        ingredient = new Ingredient();
        ingredient.setName(INGREDIENT_NAME);
        ingredient.setUnitOfMeasure(savedUnitOfMeasure);

        ingredientRepository.save(ingredient);
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
}