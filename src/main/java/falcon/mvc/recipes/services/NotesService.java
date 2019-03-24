package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.Notes;
import falcon.mvc.recipes.repositories.NotesRepository;

public interface NotesService extends BaseService<Notes, Long, NotesRepository> {

    Notes createNotes(Notes notes);
}
