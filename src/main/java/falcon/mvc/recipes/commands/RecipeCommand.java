package falcon.mvc.recipes.commands;

import falcon.mvc.recipes.domains.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {
    private Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String description;

    @Min(1)
    @Max(999)
    private Integer prepTime;

    @Min(1)
    @Max(999)
    private Integer cookTime;

    @Min(1)
    @Max(999)
    private Integer servings;

    @NotBlank
    @Size(min = 3, max = 255)
    private String directions;

    private Byte[] image;
    private SortedSet<IngredientCommand> ingredients = new TreeSet<>(Comparator.comparing(IngredientCommand::getId));
    private Difficulty difficulty;
    private NotesCommand notes;
    private SortedSet<CategoryCommand> categories = new TreeSet<>(Comparator.comparing(CategoryCommand::getId));
}
