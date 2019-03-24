package falcon.mvc.recipes.repositories;

import falcon.mvc.recipes.domains.Notes;
import org.springframework.data.repository.CrudRepository;

public interface NotesRepository extends CrudRepository<Notes, Long> {
}
