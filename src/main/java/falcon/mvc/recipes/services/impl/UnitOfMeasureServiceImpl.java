package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.domains.UnitOfMeasure;
import falcon.mvc.recipes.repositories.UnitOfMeasureRepository;
import falcon.mvc.recipes.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }


    @Override
    public UnitOfMeasure getByUnit(String unit) {
        if (unitOfMeasureRepository.findByUnit(unit).isPresent()) {
            log.debug("Searching for unit of measure with name " + unit);
            return unitOfMeasureRepository.findByUnit(unit).get();
        }else {
            throw new RuntimeException("No such Unit of Measure!");
        }
    }

    @Override
    public UnitOfMeasure createUnit(UnitOfMeasure unit) {
        log.debug("Saving new unit of measure " + unit);
        return unitOfMeasureRepository.save(unit);
    }
}
