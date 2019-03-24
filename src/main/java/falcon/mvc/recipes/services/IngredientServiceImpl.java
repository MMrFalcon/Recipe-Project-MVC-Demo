package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.Ingredient;
import falcon.mvc.recipes.repositories.IngredientRepository;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient getByName(String name) {
        if (ingredientRepository.findByName(name).isPresent()) {
            return ingredientRepository.findByName(name).get();
        }else {
            throw new RuntimeException("No such Ingredient!");
        }
    }
}
