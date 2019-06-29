package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.Notes;
import falcon.mvc.recipes.domains.Recipe;
import falcon.mvc.recipes.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageServiceTestIT {

    private static final Long RECIPE_ID = 1L;

    private Recipe recipe;

    @Autowired
    private ImageService imageService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {
        recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setNotes(new Notes());

    }

    @Test
    public void addImage() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile("imageFile", "testing.txt", "text/plain",
                "Bytes for save".getBytes());

        imageService.addImage(RECIPE_ID, multipartFile);

        assertEquals(recipeRepository.findById(RECIPE_ID).get().getImg().length, multipartFile.getBytes().length);
    }
}