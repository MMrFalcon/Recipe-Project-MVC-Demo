package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.UnitOfMeasure;

public interface UnitOfMeasureService {

    UnitOfMeasure getByUnit(String unit);
    UnitOfMeasure createUnit(UnitOfMeasure unit);
}
