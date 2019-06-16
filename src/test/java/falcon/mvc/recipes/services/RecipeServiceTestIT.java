package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.NotesCommand;
import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.converters.RecipeToRecipeCommand;
import falcon.mvc.recipes.domains.Notes;
import falcon.mvc.recipes.domains.Recipe;
import falcon.mvc.recipes.repositories.RecipeRepository;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipeServiceTestIT {

    private static final String NEW_DESCRIPTION = "New Description";

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeToRecipeCommand recipeToRecipeCommand;


    @Test
    public void getAllRecipes() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setNotes(new Notes());

        Recipe secondRecipe = new Recipe();
        secondRecipe.setId(2L);
        secondRecipe.setNotes(new Notes());

        Set<Recipe> recipesData = new HashSet<>();
        recipesData.add(recipe);
        recipesData.add(secondRecipe);

        recipeRepository.saveAll(recipesData);

        List<RecipeCommand> recipes = recipeService.getAllRecipes();

        assertEquals(recipes.size(), 2);
    }

    @Transactional
    @Test
    public void saveRecipeCommand() throws Exception {

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
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        recipeCommand.setNotes(new NotesCommand());
        recipeService.saveRecipeCommand(recipeCommand);
        RecipeCommand returnedRecipe = recipeService.getRecipeCommandById(1L);

        assertNotNull(returnedRecipe);
        assertEquals(returnedRecipe.getId(), recipeCommand.getId());
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Transactional
    @Test
    public void getRecipeCommandByIdNotPresent() {
        final String exceptionMessage = "Recipe not found";
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage(exceptionMessage);

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        recipeCommand.setNotes(new NotesCommand());
        recipeService.saveRecipeCommand(recipeCommand);
        recipeService.getRecipeCommandById(13L);
    }

}