package falcon.mvc.recipes.controllers;

import falcon.mvc.recipes.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {


    private RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"","/index","/index.html"})
    public String getIndex(Model model){

        model.addAttribute("recipes", recipeService.getAllRecipes());
        return "index";
    }
}
