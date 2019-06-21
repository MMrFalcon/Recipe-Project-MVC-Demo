package falcon.mvc.recipes.bootstrap;

import falcon.mvc.recipes.converters.CategoryCommandToCategory;
import falcon.mvc.recipes.converters.UnitOfMeasureCommandToUnitOfMeasure;
import falcon.mvc.recipes.domains.*;
import falcon.mvc.recipes.repositories.RecipeRepository;
import falcon.mvc.recipes.services.CategoryService;
import falcon.mvc.recipes.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryService categoryService;
    private final UnitOfMeasureService unitOfMeasureService;
    private final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;
    private final CategoryCommandToCategory categoryCommandToCategory;
    private final RecipeRepository recipeRepository;

    public DataInitializer(CategoryService categoryService, UnitOfMeasureService unitOfMeasureService,
                           UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure,
                           CategoryCommandToCategory categoryCommandToCategory, RecipeRepository recipeRepository) {

        this.categoryService = categoryService;
        this.unitOfMeasureService = unitOfMeasureService;
        this.unitOfMeasureCommandToUnitOfMeasure = unitOfMeasureCommandToUnitOfMeasure;
        this.categoryCommandToCategory = categoryCommandToCategory;
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.debug("Loading data on starts up");
        recipeRepository.saveAll(recipesInitialization());

    }

    private Set<Recipe> recipesInitialization() {

        Set<Recipe> recipes = new HashSet<>();

        UnitOfMeasure each = unitOfMeasureCommandToUnitOfMeasure.convert(unitOfMeasureService.getUnitOfMeasureByUnit("each"));
        UnitOfMeasure pinch = unitOfMeasureCommandToUnitOfMeasure.convert(unitOfMeasureService.getUnitOfMeasureByUnit("pinch"));
        UnitOfMeasure tablespoon = unitOfMeasureCommandToUnitOfMeasure.convert(unitOfMeasureService.getUnitOfMeasureByUnit("tablespoon"));
        UnitOfMeasure teaspoon = unitOfMeasureCommandToUnitOfMeasure.convert(unitOfMeasureService.getUnitOfMeasureByUnit("teaspoon"));
        log.debug(String.valueOf(unitOfMeasureService));

        //Guacamole
        Recipe recipe = new Recipe();
        recipe.setPrepTime(30);
        recipe.setCookTime(0);
        recipe.setDescription("Guacamole");
        recipe.setDifficulty(Difficulty.EASY);
        recipe.setServings(30);
        recipe.setDirections("Get guacamole - smash guacamole - eat");

        Notes guacamoleNotes = new Notes();
        guacamoleNotes.setRecipeNotes("can informally be referred tae \nas \"guac\" in North Americae)" +
                " \n is an avocado-based dip or salad\nfirst created by the Aztecs in whit is nou Mexico..");
        guacamoleNotes.setRecipe(recipe);
        recipe.setNotes(guacamoleNotes);

        Set<Category> categories = new HashSet<>();
        categories.add(categoryCommandToCategory.convert(categoryService.getCategoryByDescription("Vegan")));
        categories.add(categoryCommandToCategory.convert(categoryService.getCategoryByDescription("Mexican")));

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
        recipe2.setServings(60);
        recipe2.setDirections("Just do it!");


        Notes tacoNotes = new Notes();
        tacoNotes.setRecipeNotes("Is a traditional Mexican dish consisting\nof a corn or wheat tortilla " +
                "\nfolded or rolled around a filling.\nA taco can be made with a variety of fillings," +
                "\nincluding beef, pork, chicken,\nseafood, vegetables, and cheese, allowing great " +
                "\nversatility and variety.\nTacos are generally eaten without utensils, " +
                "\noften garnished with salsa,\nchili pepper, avocado, guacamole, cilantro (coriander)," +
                "\ntomatoes, onions, and lettuce.");
        tacoNotes.setRecipe(recipe2);
        recipe2.setNotes(tacoNotes);

        recipe2.getCategories().add(categoryCommandToCategory.convert(categoryService.getCategoryByDescription("Mexican")));

        recipe2.addIngredient(new Ingredient("Avocado", new BigDecimal(4), each));
        recipe2.addIngredient(new Ingredient("Salt", new BigDecimal(2), pinch));
        recipe2.addIngredient(new Ingredient("Chili pepper", new BigDecimal(3), each));
        recipe2.addIngredient(new Ingredient("Oil", new BigDecimal(1), tablespoon));
        recipe2.addIngredient(new Ingredient("Flour", new BigDecimal(5), teaspoon));

        recipes.add(recipe2);

        return recipes;
    }


}
