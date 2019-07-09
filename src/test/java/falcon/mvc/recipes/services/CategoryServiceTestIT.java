package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.CategoryCommand;
import falcon.mvc.recipes.exceptions.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTestIT {

    private static final String CATEGORY_FROM_DB_NAME = "Oriental-Food";
    private static final String CATEGORY_NAME = "New-Category";

    private CategoryCommand categoryCommand;

    @Autowired
    private CategoryService categoryService;

    @Before
    public void setUp() throws Exception {
        categoryCommand = new CategoryCommand();
        categoryCommand.setDescription(CATEGORY_NAME);
    }

    @Test
    public void getCategoryByDescription() {
        CategoryCommand foundCategory = categoryService.getCategoryByDescription(CATEGORY_FROM_DB_NAME);

        assertNotNull(foundCategory);
        assertEquals(foundCategory.getDescription(), CATEGORY_FROM_DB_NAME);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getCategoryByDescriptionNotPresent() {
        final String exceptionMessage = "No such category";
        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage(exceptionMessage);
        categoryService.getCategoryByDescription("Not Exist");
    }

    @Test
    public void createCategory() {
       CategoryCommand createdCategory = categoryService.createCategory(categoryCommand);
       assertNotNull(createdCategory);
       assertEquals(createdCategory.getDescription(), categoryCommand.getDescription());
    }
}