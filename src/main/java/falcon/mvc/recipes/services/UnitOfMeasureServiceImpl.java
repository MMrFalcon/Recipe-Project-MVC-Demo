package falcon.mvc.recipes.services;

import falcon.mvc.recipes.domains.UnitOfMeasure;
import falcon.mvc.recipes.repositories.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }


    @Override
    public UnitOfMeasure getByUnit(String unit) {
        if (unitOfMeasureRepository.findByUnit(unit).isPresent()) {
            return unitOfMeasureRepository.findByUnit(unit).get();
        }else {
            throw new RuntimeException("No such Unit of Measure!");
        }
    }

    @Override
    public UnitOfMeasure createUnit(UnitOfMeasure unit) {
        return unitOfMeasureRepository.save(unit);
    }
}
