package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.UnitOfMeasure;
import falcon.mvc.recipes.repositories.UnitOfMeasureRepository;

public interface UnitOfMeasureService extends BaseService<UnitOfMeasure, Long, UnitOfMeasureRepository> {

    UnitOfMeasure getByUnit(String unit);
    UnitOfMeasure createUnit(UnitOfMeasure unit);
}
