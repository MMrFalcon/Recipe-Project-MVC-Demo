package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.NotesCommand;
import falcon.mvc.recipes.converters.NotesCommandToNotes;
import falcon.mvc.recipes.converters.NotesToNotesCommand;
import falcon.mvc.recipes.domains.Notes;
import falcon.mvc.recipes.repositories.NotesRepository;
import falcon.mvc.recipes.services.NotesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepository;
    private final NotesToNotesCommand notesToNotesCommand;
    private final NotesCommandToNotes notesCommandToNotes;

    public NotesServiceImpl(NotesRepository notesRepository, NotesToNotesCommand notesToNotesCommand,
                            NotesCommandToNotes notesCommandToNotes) {

        this.notesRepository = notesRepository;
        this.notesToNotesCommand = notesToNotesCommand;
        this.notesCommandToNotes = notesCommandToNotes;
    }

    @Override
    public NotesCommand createNotes(NotesCommand notes) {
        log.debug("Saving notes " + notes);
        Notes savedNotes = notesRepository.save(notesCommandToNotes.convert(notes));
        return notesToNotesCommand.convert(savedNotes);
    }
}
