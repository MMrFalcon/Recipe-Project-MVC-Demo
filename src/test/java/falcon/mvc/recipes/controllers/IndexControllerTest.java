package falcon.mvc.recipes.controllers;

import falcon.mvc.recipes.domains.Recipe;
import falcon.mvc.recipes.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class IndexControllerTest {

    private IndexController indexController;

    @Mock
    Model model;

    @Mock
    RecipeService recipeService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeService);
    }

    @Test
    public void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void getIndex() {

        //given
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(new Recipe());
        Recipe secondRecipe = new Recipe();
        secondRecipe.setDescription("Different than first");
        recipes.add(secondRecipe);
        recipes.add(secondRecipe);

        when(recipeService.getAllRecipes()).thenReturn(recipes);
        ArgumentCaptor argumentCaptor = ArgumentCaptor.forClass(Set.class);
        //when
        String viewName = indexController.getIndex(model);

        //then
        assertEquals("index", viewName);
        verify(recipeService, times(1)).getAllRecipes();
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        Set<Recipe> setInController = (Set<Recipe>) argumentCaptor.getValue();
        assertEquals(setInController.size(), 2L);
    }
}