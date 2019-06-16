package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.NotesCommand;
import falcon.mvc.recipes.converters.NotesCommandToNotes;
import falcon.mvc.recipes.converters.NotesToNotesCommand;
import falcon.mvc.recipes.domains.Notes;
import falcon.mvc.recipes.repositories.NotesRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotesServiceImplTest {

    private NotesServiceImpl notesService;
    private Notes notesFromRepository;
    private NotesCommand notesCommand;

    private static final Long NOTES_ID = 1L;
    private static final String NOTES = "Some Notes";

    @Mock
    NotesRepository notesRepository;

    @Mock
    NotesToNotesCommand notesToNotesCommand;

    @Mock
    NotesCommandToNotes notesCommandToNotes;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        notesService = new NotesServiceImpl(notesRepository, notesToNotesCommand, notesCommandToNotes);

        notesFromRepository = new Notes();
        notesFromRepository.setId(NOTES_ID);
        notesFromRepository.setRecipeNotes(NOTES);

        notesCommand = new NotesCommand();
        notesCommand.setId(NOTES_ID);
        notesCommand.setRecipeNotes(NOTES);
    }

    @Test
    public void createNotes() {
        when(notesRepository.save(any())).thenReturn(notesFromRepository);
        when(notesCommandToNotes.convert(notesCommand)).thenReturn(notesFromRepository);
        when(notesToNotesCommand.convert(notesFromRepository)).thenReturn(notesCommand);

        NotesCommand savedNotes = notesService.createNotes(notesCommand);

        assertNotNull(savedNotes);
        verify(notesRepository, times(1)).save(any());
        verify(notesCommandToNotes, times(1)).convert(notesCommand);
        verify(notesToNotesCommand, times(1)).convert(notesFromRepository);
    }
}