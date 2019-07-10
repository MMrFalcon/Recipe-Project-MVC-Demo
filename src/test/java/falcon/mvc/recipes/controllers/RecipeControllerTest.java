package falcon.mvc.recipes.controllers;

import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.exceptions.NotFoundException;
import falcon.mvc.recipes.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    private static final Long RECIPE_COMMAND_ID = 1L;

    private RecipeCommand recipeCommand;

    @Mock
    private RecipeService recipeService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RecipeController recipeController = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();

        recipeCommand = new RecipeCommand();
    }

    @Test
    public void showRecipe() throws Exception {
        recipeCommand.setId(RECIPE_COMMAND_ID);

        when(recipeService.getRecipeCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void getNewRecipeForm() throws Exception {

        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeForm"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void saveOrUpdateRecipe() throws Exception {
        recipeCommand.setId(RECIPE_COMMAND_ID);

        when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some string")
        )
                .andExpect(status().isFound())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/show/"));
    }

    @Test
    public void updateRecipe() throws Exception {
        when(recipeService.getRecipeCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeForm"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void deleteRecipe() throws Exception {
        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/index"));

        verify(recipeService, times(1)).deleteById(anyLong());
    }

    @Test
    public void handleNotFoundForShowRequest() throws Exception {
        when(recipeService.getRecipeCommandById(anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name("notFoundExceptionView"));
    }

    @Test
    public void handleNotFoundForUpdateRequest() throws Exception {
        when(recipeService.getRecipeCommandById(anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(model().attributeExists("exception"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("notFoundExceptionView"));
    }

    @Test
    public void handleNumberFormatForShowRequest() throws Exception {
        mockMvc.perform(get("/recipe/asd/show"))
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name("numberFormatExceptionView"));
    }

    @Test
    public void handleNumberFormatForUpdateRequest() throws Exception {
        mockMvc.perform(get("/recipe/asd/update"))
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name("numberFormatExceptionView"));
    }
}