package falcon.mvc.recipes.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void addImage(Long recipeId, MultipartFile file);
}
