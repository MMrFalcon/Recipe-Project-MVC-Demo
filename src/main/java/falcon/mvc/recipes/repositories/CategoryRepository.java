package falcon.mvc.recipes.repositories;

import falcon.mvc.recipes.domains.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
