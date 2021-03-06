package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.NotesCommand;
import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.converters.RecipeToRecipeCommand;
import falcon.mvc.recipes.domains.Notes;
import falcon.mvc.recipes.domains.Recipe;
import falcon.mvc.recipes.exceptions.NotFoundException;
import falcon.mvc.recipes.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipeServiceTestIT {

    private static final String NEW_DESCRIPTION = "New Description";
    private static final Long RECIPE_ID = 1L;
    private static final Long RECIPE_FOR_DELETE_ID = 2L;
    private static final byte[] RECIPE_IMAGE = "Image".getBytes();

    private RecipeCommand recipeCommand;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeToRecipeCommand recipeToRecipeCommand;

    @Before
    public void setUp() throws Exception {
        recipeCommand = new RecipeCommand();
        recipeCommand.setId(RECIPE_ID);
        recipeCommand.setNotes(new NotesCommand());

        Byte[] wrappedImage = new Byte[RECIPE_IMAGE.length];
        int wrappedImageIndex = 0;
        for (byte b : RECIPE_IMAGE) {
            wrappedImage[wrappedImageIndex++] = b;
        }

        recipeCommand.setImage(wrappedImage);
    }

    @Transactional
    @Test
    public void getAllRecipes() {
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        recipe.setNotes(new Notes());

        Recipe secondRecipe = new Recipe();
        secondRecipe.setId(2L);
        secondRecipe.setNotes(new Notes());

        Set<Recipe> recipesData = new HashSet<>();
        recipesData.add(recipe);
        recipesData.add(secondRecipe);

        recipeRepository.saveAll(recipesData);

        Set<Recipe> recipesFromRepo = new HashSet<>();
        recipeRepository.findAll().forEach(recipesFromRepo::add);

        List<RecipeCommand> recipes = recipeService.getAllRecipes();
        assertEquals(recipes.size(), recipesFromRepo.size());
    }

    @Transactional
    @Test
    public void saveRecipeCommand() {

        Iterable<Recipe> recipes = recipeRepository.findAll();
        Recipe testRecipe = recipes.iterator().next();
        RecipeCommand testRecipeCommand = recipeToRecipeCommand.convert(testRecipe);

        if (testRecipeCommand != null) {
            testRecipeCommand.setDescription(NEW_DESCRIPTION);
        }
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(testRecipeCommand);

        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
        assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());
        assertEquals(testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
    }


    @Transactional
    @Test
    public void getRecipeCommandById() {

        recipeService.saveRecipeCommand(recipeCommand);
        RecipeCommand returnedRecipe = recipeService.getRecipeCommandById(RECIPE_ID);

        assertNotNull(returnedRecipe);
        assertEquals(returnedRecipe.getId(), recipeCommand.getId());
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Transactional
    @Test
    public void getRecipeCommandByIdNotPresent() {
        final String exceptionMessage = "Recipe not found";
        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage(exceptionMessage);

        recipeService.saveRecipeCommand(recipeCommand);
        recipeService.getRecipeCommandById(13L);
    }

    @Test
    public void deleteRecipeById() {

        Notes notes = new Notes();
        notes.setRecipeNotes("Some Notes here");

        Recipe recipe = new Recipe();
        recipe.setNotes(notes);

        Recipe savedRecipe = recipeRepository.save(recipe);
        recipeService.deleteById(savedRecipe.getId());

        assertFalse(recipeRepository.findById(savedRecipe.getId()).isPresent());
    }

    @Test
    public void getUnboxedImage() {
        byte[] wrappedImage = recipeService.getUnboxedImage(recipeCommand);
        assertEquals(RECIPE_IMAGE.length, wrappedImage.length);
    }
}