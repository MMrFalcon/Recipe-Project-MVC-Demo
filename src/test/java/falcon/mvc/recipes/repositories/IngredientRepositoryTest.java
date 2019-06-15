package falcon.mvc.recipes.repositories;

import falcon.mvc.recipes.domains.Ingredient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IngredientRepositoryTest {

    private static final String FIRST_INGREDIENT = "Salt";
    private static final String SECOND_INGREDIENT = "Pepper";

    @Autowired
    IngredientRepository ingredientRepository;

    @Transactional
    @Test
    public void findByName() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(FIRST_INGREDIENT);

        Ingredient secondIngredient = new Ingredient();
        secondIngredient.setName(SECOND_INGREDIENT);

        ingredientRepository.save(ingredient);
        ingredientRepository.save(secondIngredient);

        Optional<Ingredient> foundIngredient = ingredientRepository.findByName(ingredient.getName());

        assertNotNull(foundIngredient.get());
        assertEquals(ingredient, foundIngredient.get());
        assertNotEquals(secondIngredient, foundIngredient.get());
    }
}