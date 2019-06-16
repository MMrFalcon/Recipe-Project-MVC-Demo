package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.UnitOfMeasureCommand;
import falcon.mvc.recipes.converters.UnitOfMeasureCommandToUnitOfMeasure;
import falcon.mvc.recipes.converters.UnitOfMeasureToUnitOfMeasureCommand;
import falcon.mvc.recipes.domains.UnitOfMeasure;
import falcon.mvc.recipes.repositories.UnitOfMeasureRepository;
import falcon.mvc.recipes.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;
    private final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository,
                                    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand,
                                    UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure) {

        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
        this.unitOfMeasureCommandToUnitOfMeasure = unitOfMeasureCommandToUnitOfMeasure;
    }


    @Override
    public UnitOfMeasureCommand getUnitOfMeasureByUnit(String unit) {
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByUnit(unit);
        if (unitOfMeasureOptional.isPresent()) {
            log.debug("Searching for unit of measure with name " + unit);
            return unitOfMeasureToUnitOfMeasureCommand.convert(unitOfMeasureOptional.get());
        }else {
            throw new RuntimeException("No such Unit of Measure!");
        }
    }

    @Override
    public UnitOfMeasureCommand createUnit(UnitOfMeasureCommand unit) {
        log.debug("Saving new unit of measure " + unit);
        UnitOfMeasure savedUnitOfMeasure = unitOfMeasureRepository.save(unitOfMeasureCommandToUnitOfMeasure.convert(unit));
        return unitOfMeasureToUnitOfMeasureCommand.convert(savedUnitOfMeasure);
    }
}
