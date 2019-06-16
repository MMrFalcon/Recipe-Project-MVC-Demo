package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.CategoryCommand;
import falcon.mvc.recipes.converters.CategoryCommandToCategory;
import falcon.mvc.recipes.converters.CategoryToCategoryCommand;
import falcon.mvc.recipes.domains.Category;
import falcon.mvc.recipes.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {

    private final static Long CATEGORY_ID = 1L;
    private final static String CATEGORY_DESCRIPTION = "Description";

    private CategoryServiceImpl categoryService;
    private Category categoryFromRepository;
    private CategoryCommand categoryCommand;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryToCategoryCommand categoryToCategoryCommand;

    @Mock
    private CategoryCommandToCategory categoryCommandToCategory;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        categoryService = new CategoryServiceImpl(categoryRepository, categoryToCategoryCommand, categoryCommandToCategory);

        categoryFromRepository = new Category();
        categoryFromRepository.setId(CATEGORY_ID);
        categoryFromRepository.setDescription(CATEGORY_DESCRIPTION);

        categoryCommand = new CategoryCommand();
        categoryCommand.setId(CATEGORY_ID);
        categoryCommand.setDescription(CATEGORY_DESCRIPTION);
    }

    @Test
    public void getCategoryByDescription() {
        Optional<Category> optionalCategory = Optional.of(categoryFromRepository);

        when(categoryRepository.findByDescription(CATEGORY_DESCRIPTION)).thenReturn(optionalCategory);
        when(categoryToCategoryCommand.convert(any())).thenReturn(categoryCommand);

        CategoryCommand foundCategory = categoryService.getCategoryByDescription(CATEGORY_DESCRIPTION);

        assertNotNull(foundCategory);
        verify(categoryRepository, times(1)).findByDescription(CATEGORY_DESCRIPTION);
        verify(categoryToCategoryCommand, times(1)).convert(any());
    }

    @Test
    public void createCategory() {
        when(categoryCommandToCategory.convert(categoryCommand)).thenReturn(categoryFromRepository);
        when(categoryRepository.save(categoryFromRepository)).thenReturn(categoryFromRepository);
        when(categoryToCategoryCommand.convert(categoryFromRepository)).thenReturn(categoryCommand);

        CategoryCommand savedCategory = categoryService.createCategory(categoryCommand);

        assertNotNull(savedCategory);
        verify(categoryRepository, times(1)).save(categoryFromRepository);
        verify(categoryCommandToCategory, times(1)).convert(categoryCommand);
        verify(categoryToCategoryCommand, times(1)).convert(categoryFromRepository);
    }
}