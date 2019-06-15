package falcon.mvc.recipes.converters;

import falcon.mvc.recipes.commands.IngredientCommand;
import falcon.mvc.recipes.domains.Ingredient;
import falcon.mvc.recipes.domains.Recipe;
import falcon.mvc.recipes.domains.UnitOfMeasure;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IngredientToIngredientCommandTest {

    private static final Recipe RECIPE = new Recipe();
    private static final BigDecimal AMOUNT = new BigDecimal("1");
    private static final String DESCRIPTION = "Cheeseburger";
    private static final Long UOM_ID = 2L;
    private static final Long ID_VALUE = 1L;

    private IngredientToIngredientCommand converter;

    @Before
    public void setUp() throws Exception {
        converter = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testNullConvert() throws Exception {
        final String exceptionMessage = "Source object is null";
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage(exceptionMessage);
        converter.convert(null);
    }

    @Test
    public void testEmptyObject() throws Exception {
        final String exceptionMessage = "Source object is null";
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage(exceptionMessage);
        converter.convert(new Ingredient());
    }

    @Test
    public void testConvertNullUOM() throws Exception {
        final String exceptionMessage = "Source object is null";
        exceptionRule.expect(NullPointerException.class);
        exceptionRule.expectMessage(exceptionMessage);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ID_VALUE);
        ingredient.setRecipe(RECIPE);
        ingredient.setAmount(AMOUNT);
        ingredient.setName(DESCRIPTION);
        ingredient.setUnitOfMeasure(null);

        converter.convert(ingredient);

    }

    @Test
    public void testConvertWithUom() throws Exception {

        Ingredient ingredient = new Ingredient();
        ingredient.setId(ID_VALUE);
        ingredient.setRecipe(RECIPE);
        ingredient.setAmount(AMOUNT);
        ingredient.setName(DESCRIPTION);

        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(UOM_ID);
        ingredient.setUnitOfMeasure(uom);

        IngredientCommand ingredientCommand = converter.convert(ingredient);

        assertEquals(ID_VALUE, ingredientCommand.getId());
        assertNotNull(ingredientCommand.getUnitOfMeasure());
        assertEquals(UOM_ID, ingredientCommand.getUnitOfMeasure().getId());
        assertEquals(AMOUNT, ingredientCommand.getAmount());
        assertEquals(DESCRIPTION, ingredientCommand.getName());
    }
}