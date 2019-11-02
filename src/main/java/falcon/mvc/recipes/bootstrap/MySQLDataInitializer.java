package falcon.mvc.recipes.bootstrap;

import falcon.mvc.recipes.domains.Category;
import falcon.mvc.recipes.domains.UnitOfMeasure;
import falcon.mvc.recipes.repositories.CategoryRepository;
import falcon.mvc.recipes.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"dev", "prod"})
public class MySQLDataInitializer implements ApplicationListener {

    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public MySQLDataInitializer(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (categoryRepository.count() == 0 ) {
            log.debug("Loading categories...");
            loadCategories();
        }

        if (unitOfMeasureRepository.count() == 0 ) {
            log.debug("Loading Units of measures...");
            loadUnits();
        }
    }

    private void loadUnits() {
        UnitOfMeasure teaspoon = new UnitOfMeasure();
        teaspoon.setUnit("Teaspoon");
        unitOfMeasureRepository.save(teaspoon);

        UnitOfMeasure tablespoon = new UnitOfMeasure();
        tablespoon.setUnit("Tablespoon");
        unitOfMeasureRepository.save(tablespoon);

        UnitOfMeasure cup = new UnitOfMeasure();
        cup.setUnit("Cup");
        unitOfMeasureRepository.save(cup);

        UnitOfMeasure pinch = new UnitOfMeasure();
        pinch.setUnit("Pinch");
        unitOfMeasureRepository.save(pinch);

        UnitOfMeasure each = new UnitOfMeasure();
        each.setUnit("Each");
        unitOfMeasureRepository.save(each);

        UnitOfMeasure dash = new UnitOfMeasure();
        dash.setUnit("Dash");
        unitOfMeasureRepository.save(dash);

        UnitOfMeasure pint = new UnitOfMeasure();
        pint.setUnit("Pint");
        unitOfMeasureRepository.save(pint);
    }

    private void loadCategories() {
        Category vegan = new Category();
        vegan.setDescription("Vegan");
        categoryRepository.save(vegan);

        Category mexican = new Category();
        mexican.setDescription("Mexican");
        categoryRepository.save(mexican);

        Category fastFood =  new Category();
        fastFood.setDescription("Fast Food");
        categoryRepository.save(fastFood);
    }
}
