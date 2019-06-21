package falcon.mvc.recipes.controllers;

import falcon.mvc.recipes.commands.IngredientCommand;
import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.services.IngredientService;
import falcon.mvc.recipes.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {

    private static final Long RECIPE_COMMAND_ID = 1L;
    private static final Long INGREDIENT_COMMAND_ID = 2L;

    private RecipeCommand recipeCommand;
    private IngredientCommand ingredientCommand;

    @Mock
    private RecipeService recipeService;

    @Mock
    private IngredientService ingredientService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        IngredientController ingredientController = new IngredientController(recipeService, ingredientService);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();

        recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_COMMAND_ID);

        ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(RECIPE_COMMAND_ID);
        ingredientCommand.setId(INGREDIENT_COMMAND_ID);
    }

    @Test
    public void showIngredientsList() throws Exception{

        when(recipeService.getRecipeCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientList"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).getRecipeCommandById(anyLong());
    }

    @Test
    public void showRecipeIngredient() throws Exception {

        when(ingredientService.getIngredientByRecipeIdAndIngredientId(RECIPE_COMMAND_ID, INGREDIENT_COMMAND_ID))
                .thenReturn(ingredientCommand);

        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/showIngredient"))
                .andExpect(model().attributeExists("ingredient"));
    }
}