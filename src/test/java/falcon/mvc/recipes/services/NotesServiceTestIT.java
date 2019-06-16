package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.NotesCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotesServiceTestIT {

    private static final String NOTES_FOR_RECIPE = "Example notes";

    private NotesCommand notesCommand;

    @Autowired
    private NotesService notesService;

    @Before
    public void setUp() throws Exception {
        notesCommand = new NotesCommand();
        notesCommand.setRecipeNotes(NOTES_FOR_RECIPE);
    }

    @Test
    public void createNotes() {
        NotesCommand savedNotes = notesService.createNotes(notesCommand);
        assertNotNull(savedNotes);
        assertEquals(savedNotes.getRecipeNotes(), notesCommand.getRecipeNotes());
    }
}