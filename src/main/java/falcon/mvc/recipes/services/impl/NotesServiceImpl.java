package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.domains.Notes;
import falcon.mvc.recipes.repositories.NotesRepository;
import falcon.mvc.recipes.services.NotesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepository;

    public NotesServiceImpl(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    @Override
    public Notes createNotes(Notes notes) {
        log.debug("Saving notes " + notes);
        return notesRepository.save(notes);
    }
}
