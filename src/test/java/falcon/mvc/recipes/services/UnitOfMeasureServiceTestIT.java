package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.UnitOfMeasureCommand;
import falcon.mvc.recipes.domains.UnitOfMeasure;
import falcon.mvc.recipes.exceptions.AlreadyExistException;
import falcon.mvc.recipes.exceptions.NotFoundException;
import falcon.mvc.recipes.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitOfMeasureServiceTestIT {

    private static final String UNIT_OF_MEASURE_PIECE = "piece";
    private static final String IMPORTED_UNIT_OF_MEASURE_TABLE_SPOON = "tablespoon";
    private static final String UNIT_OF_MEASURE_FOR_ADD = "some new value";
    private static final Long IMPORTED_UNIT_OF_MEASURE_ID = 1L;
    private static final Long NOT_EXISTING_UOM_ID = 9876554123L;

    private UnitOfMeasureCommand unitOfMeasureCommand;
    private UnitOfMeasureCommand unitOfMeasureCommandAlreadyInSystem;

    @Autowired
    private UnitOfMeasureService unitOfMeasureService;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Before
    public void setUp() throws Exception {
        unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setUnit(UNIT_OF_MEASURE_FOR_ADD);

        unitOfMeasureCommandAlreadyInSystem = new UnitOfMeasureCommand();
        unitOfMeasureCommandAlreadyInSystem.setUnit(IMPORTED_UNIT_OF_MEASURE_TABLE_SPOON);
    }

    @Test
    public void getUnitOfMeasureByUnit() {
        UnitOfMeasureCommand foundUnitOfMeasure = unitOfMeasureService.getUnitOfMeasureByUnit(IMPORTED_UNIT_OF_MEASURE_TABLE_SPOON);

        assertNotNull(foundUnitOfMeasure);
        assertEquals(foundUnitOfMeasure.getUnit(), IMPORTED_UNIT_OF_MEASURE_TABLE_SPOON);
        assertNotEquals(foundUnitOfMeasure.getUnit(), UNIT_OF_MEASURE_PIECE);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getUnitOfMeasureByUnitNotPresent() {
        final String exceptionMessage = "No such Unit of Measure!";
        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage(exceptionMessage);
        unitOfMeasureService.getUnitOfMeasureByUnit("Does not exist");
    }

    @Test
    public void createUnit() {
        UnitOfMeasureCommand savedUnitOfMeasure = unitOfMeasureService.createUnit(unitOfMeasureCommand);

        assertNotNull(savedUnitOfMeasure);
        assertEquals(unitOfMeasureCommand.getUnit(), savedUnitOfMeasure.getUnit());
        assertNotEquals(IMPORTED_UNIT_OF_MEASURE_TABLE_SPOON, savedUnitOfMeasure.getUnit());
    }

    @Test
    public void createUnitAlreadyExist() {
        final String exceptionMessage = "Unit of measure already exist";
        exceptionRule.expect(AlreadyExistException.class);
        exceptionRule.expectMessage(exceptionMessage);
        unitOfMeasureService.createUnit(unitOfMeasureCommandAlreadyInSystem);
    }

    @Test
    public void getAllUnitOfMeasure() {
        Set<UnitOfMeasure> importedUnitsFromRepo = new HashSet<>();
        unitOfMeasureRepository.findAll().forEach(importedUnitsFromRepo::add);

        assertEquals(importedUnitsFromRepo.size(), unitOfMeasureService.getAllUnitOfMeasure().size());
    }

    @Test
    public void getUnitOfMeasureById() {
        UnitOfMeasureCommand foundUnitOfMeasure = unitOfMeasureService.getUnitOfMeasureById(IMPORTED_UNIT_OF_MEASURE_ID);

        assertNotNull(foundUnitOfMeasure);
        assertEquals(IMPORTED_UNIT_OF_MEASURE_TABLE_SPOON, foundUnitOfMeasure.getUnit());
    }

    @Test
    public void getUnitOfMeasureByIdNotPresent() {
        final String exceptionMessage = "No such Unit of Measure!";
        exceptionRule.expect(NotFoundException.class);
        exceptionRule.expectMessage(exceptionMessage);
        unitOfMeasureService.getUnitOfMeasureById(NOT_EXISTING_UOM_ID);
    }
}