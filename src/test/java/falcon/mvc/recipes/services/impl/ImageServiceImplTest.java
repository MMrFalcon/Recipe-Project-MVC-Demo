package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ImageServiceImplTest {

    private static final Long RECIPE_ID = 1L;

    private RecipeCommand recipeCommand;

    private ImageServiceImpl imageService;

    @Mock
    private RecipeService recipeService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        imageService = new ImageServiceImpl(recipeService);

        recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
    }

    @Test
    public void addImage() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile("imageFile", "testing.txt", "text/plain",
                "Bytes for save".getBytes());

        when(recipeService.getRecipeCommandById(RECIPE_ID)).thenReturn(recipeCommand);
        ArgumentCaptor<RecipeCommand> recipeArgumentCaptor = ArgumentCaptor.forClass(RecipeCommand.class);
        imageService.addImage(RECIPE_ID, multipartFile);

        verify(recipeService, times(1)).getRecipeCommandById(RECIPE_ID);
        verify(recipeService, times(1)).saveRecipeCommand(recipeArgumentCaptor.capture());

        RecipeCommand savedRecipe = recipeArgumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }
}