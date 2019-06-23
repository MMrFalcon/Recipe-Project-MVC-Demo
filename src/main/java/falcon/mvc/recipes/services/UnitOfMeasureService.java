package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMeasureService {

    UnitOfMeasureCommand getUnitOfMeasureByUnit(String unit);
    UnitOfMeasureCommand getUnitOfMeasureById(Long id);
    UnitOfMeasureCommand createUnit(UnitOfMeasureCommand unit);
    Set<UnitOfMeasureCommand> getAllUnitOfMeasure();
}
