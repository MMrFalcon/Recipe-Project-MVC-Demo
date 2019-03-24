package falcon.mvc.recipes.repositories;

import falcon.mvc.recipes.domains.Ingredient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient,Long> {
    Optional<Ingredient> findByName(String name);
}
