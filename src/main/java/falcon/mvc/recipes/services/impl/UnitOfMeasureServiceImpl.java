package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.UnitOfMeasureCommand;
import falcon.mvc.recipes.converters.UnitOfMeasureCommandToUnitOfMeasure;
import falcon.mvc.recipes.converters.UnitOfMeasureToUnitOfMeasureCommand;
import falcon.mvc.recipes.domains.UnitOfMeasure;
import falcon.mvc.recipes.exceptions.AlreadyExistException;
import falcon.mvc.recipes.exceptions.NotFoundException;
import falcon.mvc.recipes.repositories.UnitOfMeasureRepository;
import falcon.mvc.recipes.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        log.debug("Searching for unit of measure with name " + unit);
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByUnit(unit);
        if (unitOfMeasureOptional.isPresent()) {
            return unitOfMeasureToUnitOfMeasureCommand.convert(unitOfMeasureOptional.get());
        } else {
            throw new NotFoundException("No such Unit of Measure!");
        }
    }

    @Override
    public UnitOfMeasureCommand getUnitOfMeasureById(Long id) {
        log.debug("Searching for unit of measure with id " + id);
        Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findById(id);
        if (unitOfMeasureOptional.isPresent()) {
            return unitOfMeasureToUnitOfMeasureCommand.convert(unitOfMeasureOptional.get());
        } else {
            throw new NotFoundException("No such Unit of Measure!");
        }
    }

    @Override
    public UnitOfMeasureCommand createUnit(UnitOfMeasureCommand unit) {
        unitOfMeasureRepository.findByUnit(unit.getUnit()).ifPresent(found -> {
            throw new AlreadyExistException("Unit of measure already exist");
        });

        log.debug("Saving new unit of measure " + unit);

        UnitOfMeasure savedUnitOfMeasure = unitOfMeasureRepository.save(unitOfMeasureCommandToUnitOfMeasure.convert(unit));
        return unitOfMeasureToUnitOfMeasureCommand.convert(savedUnitOfMeasure);
    }

    @Override
    public Set<UnitOfMeasureCommand> getAllUnitOfMeasure() {
        return StreamSupport.stream(unitOfMeasureRepository.findAll()
                .spliterator(), false)
                .map(unitOfMeasureToUnitOfMeasureCommand::convert)
                .collect(Collectors.toSet());
    }
}
