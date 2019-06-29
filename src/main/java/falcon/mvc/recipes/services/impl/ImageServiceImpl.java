package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.domains.Recipe;
import falcon.mvc.recipes.repositories.RecipeRepository;
import falcon.mvc.recipes.services.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void addImage(Long recipeId, MultipartFile file){
        try {
            Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
            log.debug("Searching for recipe with id: " + recipeId);
            if (!recipeOptional.isPresent())
                throw new RuntimeException("Recipe not found");

            Recipe foundRecipe = recipeOptional.get();
            Byte[] imageWrapper =  new Byte[file.getBytes().length];
            int arrayIndexNumber = 0;

            for (byte b : file.getBytes()) {
                imageWrapper[arrayIndexNumber++] = b;
            }

            foundRecipe.setImg(imageWrapper);
            log.debug("New image was added to the recipe");
            recipeRepository.save(foundRecipe);
        } catch (IOException ex) {
            log.error("Cannot save image", ex);
        }

    }
}
