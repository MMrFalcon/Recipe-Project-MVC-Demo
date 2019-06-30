package falcon.mvc.recipes.controllers;

import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.services.ImageService;
import falcon.mvc.recipes.services.RecipeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    @GetMapping("/{recipeId}/render/image")
    public void renderImageFromDb(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = recipeService.getRecipeCommandById(Long.valueOf(recipeId));

        response.setContentType("image/jpeg");
        InputStream inputStream = new ByteArrayInputStream(recipeService.getUnboxedImage(recipeCommand));
        IOUtils.copy(inputStream, response.getOutputStream());
    }
}
