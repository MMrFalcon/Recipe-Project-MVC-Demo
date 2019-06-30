package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.RecipeCommand;
import falcon.mvc.recipes.services.ImageService;
import falcon.mvc.recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final RecipeService recipeService;

    public ImageServiceImpl(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    @Transactional
    public void addImage(Long recipeId, MultipartFile file){
        try {
            log.debug("Adding new image to the recipe");
            RecipeCommand recipeCommand = recipeService.getRecipeCommandById(recipeId);

            Byte[] imageWrapper =  new Byte[file.getBytes().length];
            int arrayIndexNumber = 0;

            for (byte b : file.getBytes()) {
                imageWrapper[arrayIndexNumber++] = b;
            }

            recipeCommand.setImage(imageWrapper);
            recipeService.saveRecipeCommand(recipeCommand);
            log.debug("Image was successfully saved");
        } catch (IOException ex) {
            log.error("Cannot save image", ex);
        }

    }
}
