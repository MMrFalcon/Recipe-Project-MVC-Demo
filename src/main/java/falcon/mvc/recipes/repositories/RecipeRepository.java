package falcon.mvc.recipes.repositories;

import falcon.mvc.recipes.domains.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
}
