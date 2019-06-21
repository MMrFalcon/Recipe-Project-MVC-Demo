package falcon.mvc.recipes.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCommand {
    private Long id;
    private Long recipeId;
    private String name;
    private BigDecimal amount;
    private UnitOfMeasureCommand unitOfMeasure;
}
