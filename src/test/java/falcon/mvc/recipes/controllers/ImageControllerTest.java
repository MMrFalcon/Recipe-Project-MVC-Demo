package falcon.mvc.recipes.controllers;

import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.services.ImageService;
import falcon.mvc.recipes.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerTest {

    private static final Long RECIPE_COMMAND_ID = 1L;
    private static final byte[] RECIPE_IMAGE = "Image".getBytes();

    private RecipeCommand recipeCommand;

    private ImageController imageController;

    @Mock
    RecipeService recipeService;

    @Mock
    ImageService imageService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        imageController = new ImageController(recipeService, imageService);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();

        recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_COMMAND_ID);

        Byte[] wrappedImage = new Byte[RECIPE_IMAGE.length];
        int wrappedImageIndex = 0;
        for (byte b : RECIPE_IMAGE) {
            wrappedImage[wrappedImageIndex++] = b;
        }

        recipeCommand.setImage(wrappedImage);

    }

    @Test
    public void showImageForm() throws Exception {
        when(recipeService.getRecipeCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).getRecipeCommandById(anyLong());
    }

    @Test
    public void addImage() throws Exception {
        MockMultipartFile multipartFile =
                new MockMultipartFile("imageFile", "testing.jpg", "text/plain",
                        "Test File".getBytes());

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService, times(1)).addImage(anyLong(), any());
    }

    @Test
    public void renderImageFromDb() throws Exception {
        when(recipeService.getRecipeCommandById(RECIPE_COMMAND_ID)).thenReturn(recipeCommand);
        when(recipeService.getUnboxedImage(recipeCommand)).thenReturn(RECIPE_IMAGE);

        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/render/image"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseBytes = response.getContentAsByteArray();
        assertEquals(RECIPE_IMAGE.length, responseBytes.length);
        verify(recipeService, times(1)).getRecipeCommandById(RECIPE_COMMAND_ID);
        verify(recipeService, times(1)).getUnboxedImage(recipeCommand);
    }
}