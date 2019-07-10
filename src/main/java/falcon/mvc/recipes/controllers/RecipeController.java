package falcon.mvc.recipes.controllers;

import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.exceptions.NotFoundException;
import falcon.mvc.recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/recipe")
@Slf4j
public class RecipeController {

    private static final String RECIPE_FORM_URL = "recipe/recipeForm";
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{id}/show")
    public String showRecipe(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.getRecipeCommandById(Long.valueOf(id)));
        return "recipe/show";
    }

    @GetMapping("/new")
    public String getNewRecipeForm(Model model) {
        model.addAttribute("recipe", new RecipeCommand());

        return RECIPE_FORM_URL;
    }

    @GetMapping("/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model){
        model.addAttribute("recipe", recipeService.getRecipeCommandById(Long.valueOf(id)));
        return  RECIPE_FORM_URL;
    }

    @PostMapping
    public String saveOrUpdateRecipe(@Valid @ModelAttribute("recipe") RecipeCommand recipeCommand, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
            return RECIPE_FORM_URL;
        }

        RecipeCommand savedRecipe  =  recipeService.saveRecipeCommand(recipeCommand);
        return "redirect:/recipe/" + savedRecipe.getId() + "/show/";
    }

    @GetMapping("/{id}/delete")
    public String deleteRecipe(@PathVariable String id) {
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/index";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(Exception exception, Model model) {
        model.addAttribute("exception", exception);
        return "notFoundExceptionView";
    }

}
