package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.domains.Ingredient;
import falcon.mvc.recipes.repositories.IngredientRepository;
import falcon.mvc.recipes.services.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient getByName(String name) {
        if (ingredientRepository.findByName(name).isPresent()) {
            log.debug("Searching for Ingredient with name " + name);
            return ingredientRepository.findByName(name).get();
        }else {
            throw new RuntimeException("No such Ingredient!");
        }
    }
}
