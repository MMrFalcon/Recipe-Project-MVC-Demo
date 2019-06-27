package falcon.mvc.recipes.commands;

import falcon.mvc.recipes.domains.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {
    private Long id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String directions;
    private SortedSet<IngredientCommand> ingredients = new TreeSet<>(Comparator.comparing(IngredientCommand::getId));
    private Difficulty difficulty;
    private NotesCommand notes;
    private SortedSet<CategoryCommand> categories = new TreeSet<>(Comparator.comparing(CategoryCommand::getId));
}
