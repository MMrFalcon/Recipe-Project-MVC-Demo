package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.domains.Recipe;
import falcon.mvc.recipes.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ImageServiceImplTest {

    private static final Long RECIPE_ID = 1L;

    private Recipe recipe;

    private ImageServiceImpl imageService;

    @Mock
    private RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        imageService = new ImageServiceImpl(recipeRepository);

        recipe = new Recipe();
        recipe.setId(RECIPE_ID);
    }

    @Test
    public void addImage() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile("imageFile", "testing.txt", "text/plain",
                "Bytes for save".getBytes());
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(RECIPE_ID)).thenReturn(recipeOptional);
        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);
        imageService.addImage(RECIPE_ID, multipartFile);

        verify(recipeRepository, times(1)).findById(RECIPE_ID);
        verify(recipeRepository, times(1)).save(recipeArgumentCaptor.capture());

        Recipe savedRecipe = recipeArgumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImg().length);
    }
}