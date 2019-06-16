package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.UnitOfMeasureCommand;

public interface UnitOfMeasureService {

    UnitOfMeasureCommand getUnitOfMeasureByUnit(String unit);
    UnitOfMeasureCommand createUnit(UnitOfMeasureCommand unit);
}
