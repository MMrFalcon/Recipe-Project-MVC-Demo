package falcon.mvc.recipes.bootstrap;

import falcon.mvc.recipes.domains.*;
import falcon.mvc.recipes.services.CategoryService;
import falcon.mvc.recipes.services.IngredientService;
import falcon.mvc.recipes.services.RecipeService;
import falcon.mvc.recipes.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private RecipeService recipeService;
    private CategoryService categoryService;
    private IngredientService ingredientService;
    private UnitOfMeasureService unitOfMeasureService;

    public DataInitializer(RecipeService recipeService, CategoryService categoryService,IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("Loading data on starts up");
        recipeService.createRecipes(recipesInitialization());
    }
    private List<Recipe> recipesInitialization() {

        List<Recipe> recipes = new ArrayList<>();

        Byte[] mockImg = new Byte[1];
        mockImg[0] = 1;

        UnitOfMeasure each = unitOfMeasureService.getByUnit("each");
        UnitOfMeasure pinch= unitOfMeasureService.getByUnit("pinch");
        UnitOfMeasure tablespoon = unitOfMeasureService.getByUnit("tablespoon");
        UnitOfMeasure teaspoon = unitOfMeasureService.getByUnit("teaspoon");
        log.debug(String.valueOf(unitOfMeasureService));
        log.debug(each.getUnit() + "," + pinch.toString());
        //Guacamole
        Recipe recipe = new Recipe();
        recipe.setPrepTime(30);
        recipe.setCookTime(0);
        recipe.setDescription("Guacamole");
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setDirection("Direction");
        recipe.setImg(mockImg);
        recipe.setServings(30);


        Notes guacamoleNotes = new Notes();
        guacamoleNotes.setDescription("can informally be referred tae \nas \"guac\" in North Americae)" +
                " \n is an avocado-based dip or salad\nfirst created by the Aztecs in whit is nou Mexico..");
        guacamoleNotes.setRecipe(recipe);
        recipe.setNotes(guacamoleNotes);

        Set<Category> categories = new HashSet<>();
        categories.add(categoryService.getByDescription("Vegan"));
        categories.add(categoryService.getByDescription("Mexican"));

        recipe.setCategories(categories);

        recipe.addIngredient(new Ingredient("Avocado", new BigDecimal(2), each));
        recipe.addIngredient(new Ingredient("Salt", new BigDecimal(1), pinch));
        recipe.addIngredient(new Ingredient("Chili pepper", new BigDecimal(3), each));
        recipe.addIngredient(new Ingredient("Oil", new BigDecimal(1), tablespoon));


        recipes.add(recipe);

        //Tacos

        Recipe recipe2 = new Recipe();
        recipe2.setPrepTime(22);
        recipe2.setCookTime(0);
        recipe2.setDescription("Tacos");
        recipe2.setDifficulty(Difficulty.MODERATE);
        recipe2.setDirection("Direction");
        recipe2.setImg(mockImg);
        recipe2.setServings(60);

        Notes tacoNotes = new Notes();
        tacoNotes.setDescription("Is a traditional Mexican dish consisting\nof a corn or wheat tortilla " +
                "\nfolded or rolled around a filling.\nA taco can be made with a variety of fillings," +
                "\nincluding beef, pork, chicken,\nseafood, vegetables, and cheese, allowing great " +
                "\nversatility and variety.\nTacos are generally eaten without utensils, " +
                "\noften garnished with salsa,\nchili pepper, avocado, guacamole, cilantro (coriander)," +
                "\ntomatoes, onions, and lettuce.");
        tacoNotes.setRecipe(recipe2);
        recipe2.setNotes(tacoNotes);

       recipe2.getCategories().add(categoryService.getByDescription("Mexican"));

       recipe2.addIngredient(new Ingredient("Avocado", new BigDecimal(4), each));
       recipe2.addIngredient(new Ingredient("Salt", new BigDecimal(2), pinch));
       recipe2.addIngredient(new Ingredient("Chili pepper", new BigDecimal(3), each));
       recipe2.addIngredient(new Ingredient("Oil", new BigDecimal(1), tablespoon));
       recipe2.addIngredient(new Ingredient("Flour", new BigDecimal(5), teaspoon));

       recipes.add(recipe2);

        return recipes;
    }

}
