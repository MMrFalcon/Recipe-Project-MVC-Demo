package falcon.mvc.recipes.controllers;

import falcon.mvc.recipes.commands.IngredientCommand;
import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.commands.UnitOfMeasureCommand;
import falcon.mvc.recipes.exceptions.NotFoundException;
import falcon.mvc.recipes.services.IngredientService;
import falcon.mvc.recipes.services.RecipeService;
import falcon.mvc.recipes.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {

    private static final Long RECIPE_COMMAND_ID = 1L;
    private static final Long INGREDIENT_COMMAND_ID = 2L;

    private RecipeCommand recipeCommand;
    private IngredientCommand ingredientCommand;
    private Set<UnitOfMeasureCommand> unitOfMeasureCommandSet;

    @Mock
    private RecipeService recipeService;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private UnitOfMeasureService unitOfMeasureService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        IngredientController ingredientController = new IngredientController(recipeService, ingredientService,
                unitOfMeasureService);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();

        recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_COMMAND_ID);

        ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(RECIPE_COMMAND_ID);
        ingredientCommand.setId(INGREDIENT_COMMAND_ID);

        unitOfMeasureCommandSet = new HashSet<>();
        unitOfMeasureCommandSet.add(new UnitOfMeasureCommand());
    }

    @Test
    public void showIngredientsList() throws Exception {

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

    @Test
    public void showRecipeIngredientUpdate() throws Exception {

        when(ingredientService.getIngredientByRecipeIdAndIngredientId(RECIPE_COMMAND_ID, INGREDIENT_COMMAND_ID))
                .thenReturn(ingredientCommand);
        when(unitOfMeasureService.getAllUnitOfMeasure()).thenReturn(unitOfMeasureCommandSet);


        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientForm"))
                .andExpect(model().attributeExists("ingredient", "unitOfMeasureList"));

        verify(ingredientService, times(1))
                .getIngredientByRecipeIdAndIngredientId(RECIPE_COMMAND_ID, INGREDIENT_COMMAND_ID);
        verify(unitOfMeasureService, times(1)).getAllUnitOfMeasure();
    }

    @Test
    public void showNewIngredientForm() throws Exception {

        when(recipeService.getRecipeCommandById(RECIPE_COMMAND_ID)).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/1/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientForm"))
                .andExpect(model().attributeExists("ingredient", "unitOfMeasureList"));

        verify(recipeService, times(1)).getRecipeCommandById(RECIPE_COMMAND_ID);
    }

    @Test
    public void saveOrUpdateIngredient() throws Exception {

        when(ingredientService.createOrUpdateIngredientCommand(any())).thenReturn(ingredientCommand);

        mockMvc.perform(post("/recipe/1/ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredient/2/show"));

        verify(ingredientService, times(1)).createOrUpdateIngredientCommand(any());
    }

    @Test
    public void deleteIngredient() throws Exception {
        mockMvc.perform(get("/recipe/1/ingredient/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredients"));

        verify(ingredientService, times(1)).deleteIngredientById(anyLong());
    }

    @Test
    public void handleNotFoundForShowRequest() throws Exception {
        when(ingredientService.getIngredientByRecipeIdAndIngredientId(anyLong(), anyLong()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name("notFoundExceptionView"));
    }

    @Test
    public void handleNotFoundForUpdateRequest() throws Exception {
        when(ingredientService.getIngredientByRecipeIdAndIngredientId(anyLong(), anyLong()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name("notFoundExceptionView"));
    }

    @Test
    public void handleNumberFormatForShowRequest() throws Exception {
        mockMvc.perform(get("/recipe/1/ingredient/asd/show"))
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name("numberFormatExceptionView"));
    }

    @Test
    public void handleNumberFormatForUpdateRequest() throws Exception {
        mockMvc.perform(get("/recipe/1/ingredient/asd/update"))
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name("numberFormatExceptionView"));
    }
}