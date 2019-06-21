package falcon.mvc.recipes.controllers;

import falcon.mvc.recipes.services.IngredientService;
import falcon.mvc.recipes.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recipe")
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @GetMapping
    @RequestMapping("/{recipeId}/ingredients")
    public String showIngredientsList(@PathVariable String recipeId, Model model) {

        model.addAttribute("recipe", recipeService.getRecipeCommandById(Long.valueOf(recipeId)));

        return "recipe/ingredient/ingredientList";
    }

    @GetMapping
    @RequestMapping("/{recipeId}/ingredient/{ingredientId}/show")
    public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {

        model.addAttribute("ingredient",
                ingredientService.getIngredientByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId)));

        return "recipe/ingredient/showIngredient";
    }
}
