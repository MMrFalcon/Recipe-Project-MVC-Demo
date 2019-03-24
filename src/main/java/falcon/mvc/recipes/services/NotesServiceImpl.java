package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.Notes;
import falcon.mvc.recipes.repositories.NotesRepository;
import org.springframework.stereotype.Service;

@Service
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepository;

    public NotesServiceImpl(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @Override
    public Notes createNotes(Notes notes) {
        return notesRepository.save(notes);
    }
}
