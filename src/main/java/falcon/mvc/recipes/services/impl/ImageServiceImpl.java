package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.services.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public void addImage(Long recipeId, MultipartFile file) {

    }
}
