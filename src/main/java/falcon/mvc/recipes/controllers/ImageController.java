package falcon.mvc.recipes.controllers;

import falcon.mvc.recipes.services.ImageService;
import falcon.mvc.recipes.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/recipe")
public class ImageController {

    private final RecipeService recipeService;
    private final ImageService imageService;

    public ImageController(RecipeService recipeService, ImageService imageService) {
        this.recipeService = recipeService;
        this.imageService = imageService;
    }

    @GetMapping("/{recipeId}/image")
    public String showImageForm(@PathVariable String recipeId, Model model) {
        model.addAttribute("recipe", recipeService.getRecipeCommandById(Long.valueOf(recipeId)));

        return "recipe/imageUploadForm";
    }

    @PostMapping("/{recipeId}/image")
    public String addImage(@PathVariable String recipeId, @RequestParam("imageFile") MultipartFile file) {
        imageService.addImage(Long.valueOf(recipeId), file);

        return "redirect:/recipe/" + recipeId + "/show";
    }
}
