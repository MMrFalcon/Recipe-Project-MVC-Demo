package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.NotesCommand;
import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.converters.RecipeCommandToRecipe;
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
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;


    @Test
    public void getAllRecipes() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Recipe secondRecipe = new Recipe();
        secondRecipe.setId(2L);
        Set<Recipe> recipesData = new HashSet<>();
        recipesData.add(recipe);

        recipeRepository.saveAll(recipesData);

        Set<Recipe> recipes = recipeService.getAllRecipes();

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

    @Test
    public void createRecipes() {
        Set<Recipe> recipes = new HashSet<>();
        Recipe firstRecipe = new Recipe();
        firstRecipe.setId(1L);
        firstRecipe.setNotes(new Notes());
        recipes.add(firstRecipe);

        Recipe secondRecipe = new Recipe();
        secondRecipe.setId(2L);
        secondRecipe.setDescription("Different than first");
        secondRecipe.setNotes(new Notes());
        recipes.add(secondRecipe);

        List<RecipeCommand> createdRecipes = recipeService.createRecipes(recipes);

        assertEquals(createdRecipes.size(),recipes.size());

    }

    @Transactional
    @Test
    public void getById() {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        recipeCommand.setNotes(new NotesCommand());
        recipeService.saveRecipeCommand(recipeCommand);
        Recipe returnedRecipe = recipeService.getById(1L);

        assertNotNull(returnedRecipe);
        assertEquals(returnedRecipe.getId(), recipeCommand.getId());
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Transactional
    @Test
    public void getByIdNotPresent() {
        final String exceptionMessage = "Recipe not found";
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage(exceptionMessage);

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        recipeCommand.setNotes(new NotesCommand());
        recipeService.saveRecipeCommand(recipeCommand);
        recipeService.getById(13L);
    }

}