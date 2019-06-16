package falcon.mvc.recipes.services;

import falcon.mvc.recipes.commands.UnitOfMeasureCommand;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitOfMeasureServiceTestIT {

    private static final String UNIT_OF_MEASURE_PIECE = "piece";
    private static final String UNIT_OF_MEASURE_TABLE_SPOON = "tablespoon";

    private UnitOfMeasureCommand unitOfMeasureCommand;

    @Autowired
    private UnitOfMeasureService unitOfMeasureService;

    @Before
    public void setUp() throws Exception {
        unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setUnit(UNIT_OF_MEASURE_PIECE);
    }

    @Test
    public void getUnitOfMeasureByUnit() {
        UnitOfMeasureCommand foundUnitOfMeasure = unitOfMeasureService.getUnitOfMeasureByUnit(UNIT_OF_MEASURE_TABLE_SPOON);

        assertNotNull(foundUnitOfMeasure);
        assertEquals(foundUnitOfMeasure.getUnit(), UNIT_OF_MEASURE_TABLE_SPOON);
        assertNotEquals(foundUnitOfMeasure.getUnit(), UNIT_OF_MEASURE_PIECE);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getUnitOfMeasureByUnitNotPresent() {
        final String exceptionMessage = "No such Unit of Measure!";
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage(exceptionMessage);
        unitOfMeasureService.getUnitOfMeasureByUnit("Does not exist");
    }

    @Test
    public void createUnit() {
        UnitOfMeasureCommand savedUnitOfMeasure = unitOfMeasureService.createUnit(unitOfMeasureCommand);

        assertNotNull(savedUnitOfMeasure);
        assertEquals(unitOfMeasureCommand.getUnit(), savedUnitOfMeasure.getUnit());
        assertNotEquals(UNIT_OF_MEASURE_TABLE_SPOON, savedUnitOfMeasure.getUnit());
    }

}