package falcon.mvc.recipes.repositories;

import falcon.mvc.recipes.domains.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryRepositoryTestIT {
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void findByDescription() {
        Category category = new Category();
        category.setDescription("Description one");

        Category secondCategory = new Category();
        secondCategory.setDescription("Second description");

        categoryRepository.save(category);
        categoryRepository.save(secondCategory);

        Optional<Category> foundCategory = categoryRepository.findByDescription(category.getDescription());

        assertNotNull(foundCategory.get());
        assertEquals(foundCategory.get(), category);
        assertNotEquals(foundCategory.get(), secondCategory);
    }
}