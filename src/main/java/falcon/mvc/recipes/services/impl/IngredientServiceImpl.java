package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.IngredientCommand;
import falcon.mvc.recipes.converters.IngredientToIngredientCommand;
import falcon.mvc.recipes.domains.Ingredient;
import falcon.mvc.recipes.repositories.IngredientRepository;
import falcon.mvc.recipes.services.IngredientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientServiceImpl(IngredientRepository ingredientRepository,
                                 IngredientToIngredientCommand ingredientToIngredientCommand) {

        this.ingredientRepository = ingredientRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    @Override
    public IngredientCommand getIngredientByName(String name) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByName(name);
        if (optionalIngredient.isPresent()) {
            log.debug("Searching for Ingredient with name " + name);
            return ingredientToIngredientCommand.convert(optionalIngredient.get());
        }else {
            throw new RuntimeException("No such Ingredient!");
        }
    }
}
