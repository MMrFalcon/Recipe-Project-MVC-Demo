package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.NotesCommand;

public interface NotesService {

    NotesCommand createNotes(NotesCommand notes);
}
