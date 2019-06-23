package falcon.mvc.recipes.controllers;

import falcon.mvc.recipes.commands.IngredientCommand;
import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.commands.UnitOfMeasureCommand;
import falcon.mvc.recipes.services.IngredientService;
import falcon.mvc.recipes.services.RecipeService;
import falcon.mvc.recipes.services.UnitOfMeasureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recipe")
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService,
                                UnitOfMeasureService unitOfMeasureService) {

        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/{recipeId}/ingredients")
    public String showIngredientsList(@PathVariable String recipeId, Model model) {

        model.addAttribute("recipe", recipeService.getRecipeCommandById(Long.valueOf(recipeId)));

        return "recipe/ingredient/ingredientList";
    }

    @GetMapping("/{recipeId}/ingredient/{ingredientId}/show")
    public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {

        model.addAttribute("ingredient",
                ingredientService.getIngredientByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId)));

        return "recipe/ingredient/showIngredient";
    }

    @GetMapping("/{recipeId}/ingredient/{ingredientId}/update")
    public String showRecipeIngredientUpdate(@PathVariable String recipeId,
                                             @PathVariable String ingredientId, Model model) {

        model.addAttribute("ingredient",
                ingredientService.getIngredientByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId)));
        model.addAttribute("unitOfMeasureList", unitOfMeasureService.getAllUnitOfMeasure());

        return "recipe/ingredient/ingredientForm";
    }

    @GetMapping("/{recipeId}/ingredient/new")
    public String showNewIngredientForm(@PathVariable String recipeId, Model model) {

        RecipeCommand recipeCommand = recipeService.getRecipeCommandById(Long.valueOf(recipeId));

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeCommand.getId());

        model.addAttribute("ingredient", ingredientCommand);

        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());
        model.addAttribute("unitOfMeasureList", unitOfMeasureService.getAllUnitOfMeasure());

        return "recipe/ingredient/ingredientForm";
    }

    @PostMapping("/{recipeId}/ingredient")
    public String saveOrUpdateIngredient(@ModelAttribute IngredientCommand command, @PathVariable String recipeId){

        IngredientCommand savedIngredientCommand = ingredientService.createOrUpdateIngredientCommand(command);

        return "redirect:/recipe/" + recipeId + "/ingredient/" + savedIngredientCommand.getId() + "/show";
    }

    @GetMapping("/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable String recipeId, @PathVariable String ingredientId) {
        ingredientService.deleteIngredientById(Long.valueOf(ingredientId));
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
