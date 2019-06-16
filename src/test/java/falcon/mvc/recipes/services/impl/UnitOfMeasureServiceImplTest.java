package falcon.mvc.recipes.services.impl;

import falcon.mvc.recipes.commands.UnitOfMeasureCommand;
import falcon.mvc.recipes.converters.UnitOfMeasureCommandToUnitOfMeasure;
import falcon.mvc.recipes.converters.UnitOfMeasureToUnitOfMeasureCommand;
import falcon.mvc.recipes.domains.UnitOfMeasure;
import falcon.mvc.recipes.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class UnitOfMeasureServiceImplTest {

    private UnitOfMeasureServiceImpl unitOfMeasureService;
    private UnitOfMeasure unitOfMeasureFromRepository;
    private UnitOfMeasureCommand unitOfMeasureCommand;

    private static final Long UNIT_ID = 1L;
    private static final String UNIT_NAME = "pinch";

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @Mock
    UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

    @Mock
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand,
                unitOfMeasureCommandToUnitOfMeasure);

        unitOfMeasureFromRepository = new UnitOfMeasure();
        unitOfMeasureFromRepository.setId(UNIT_ID);
        unitOfMeasureFromRepository.setUnit(UNIT_NAME);

        unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(UNIT_ID);
        unitOfMeasureCommand.setUnit(UNIT_NAME);
    }

    @Test
    public void getUnitOfMeasureByUnit() {
        Optional<UnitOfMeasure> optionalUnitOfMeasure = Optional.of(unitOfMeasureFromRepository);

        when(unitOfMeasureRepository.findByUnit(UNIT_NAME)).thenReturn(optionalUnitOfMeasure);
        when(unitOfMeasureToUnitOfMeasureCommand.convert(any())).thenReturn(unitOfMeasureCommand);

        UnitOfMeasureCommand foundUnit = unitOfMeasureService.getUnitOfMeasureByUnit(UNIT_NAME);

        assertNotNull(foundUnit);
        assertEquals(foundUnit.getUnit(), unitOfMeasureCommand.getUnit());
        verify(unitOfMeasureRepository, times(1)).findByUnit(UNIT_NAME);
        verify(unitOfMeasureToUnitOfMeasureCommand, times(1)).convert(optionalUnitOfMeasure.get());
    }

    @Test
    public void createUnit() {
        when(unitOfMeasureCommandToUnitOfMeasure.convert(unitOfMeasureCommand)).thenReturn(unitOfMeasureFromRepository);
        when(unitOfMeasureToUnitOfMeasureCommand.convert(unitOfMeasureFromRepository)).thenReturn(unitOfMeasureCommand);
        when(unitOfMeasureRepository.save(unitOfMeasureFromRepository)).thenReturn(unitOfMeasureFromRepository);

        UnitOfMeasureCommand savedUnit = unitOfMeasureService.createUnit(unitOfMeasureCommand);

        assertNotNull(savedUnit);
        verify(unitOfMeasureRepository, times(1)).save(unitOfMeasureFromRepository);
        verify(unitOfMeasureToUnitOfMeasureCommand, times(1)).convert(unitOfMeasureFromRepository);
        verify(unitOfMeasureCommandToUnitOfMeasure, times(1)).convert(unitOfMeasureCommand);
    }
}