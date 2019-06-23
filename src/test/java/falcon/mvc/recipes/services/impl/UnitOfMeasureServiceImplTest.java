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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class UnitOfMeasureServiceImplTest {

    private UnitOfMeasureServiceImpl unitOfMeasureService;
    private UnitOfMeasure unitOfMeasureFromRepository;
    private UnitOfMeasure secondUnitOfMeasure;
    private UnitOfMeasureCommand unitOfMeasureCommand;

    private static final Long UNIT_ID = 1L;
    private static final Long SECOND_UNIT_ID = 123L;
    private static final String UNIT_NAME = "pinch";
    private static final String SECOND_UNIT_NAME = "each";

    private Set<UnitOfMeasure> unitOfMeasureSet;

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

        secondUnitOfMeasure = new UnitOfMeasure();
        secondUnitOfMeasure.setId(SECOND_UNIT_ID);
        secondUnitOfMeasure.setUnit(SECOND_UNIT_NAME);

        unitOfMeasureSet = new HashSet<>();
        unitOfMeasureSet.add(unitOfMeasureFromRepository);
        unitOfMeasureSet.add(secondUnitOfMeasure);
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
        when(unitOfMeasureRepository.findByUnit(unitOfMeasureCommand.getUnit())).thenReturn(Optional.empty());
        UnitOfMeasureCommand savedUnit = unitOfMeasureService.createUnit(unitOfMeasureCommand);

        assertNotNull(savedUnit);
        verify(unitOfMeasureRepository, times(1)).save(unitOfMeasureFromRepository);
        verify(unitOfMeasureToUnitOfMeasureCommand, times(1)).convert(unitOfMeasureFromRepository);
        verify(unitOfMeasureCommandToUnitOfMeasure, times(1)).convert(unitOfMeasureCommand);
    }

    @Test
    public void getAllUnitOfMeasure() {
       when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasureSet);
       when(unitOfMeasureToUnitOfMeasureCommand.convert(unitOfMeasureFromRepository)).thenReturn(unitOfMeasureCommand);
       when(unitOfMeasureToUnitOfMeasureCommand.convert(secondUnitOfMeasure)).thenReturn(new UnitOfMeasureCommand());

       Set<UnitOfMeasureCommand> foundUnits = unitOfMeasureService.getAllUnitOfMeasure();

       assertEquals(2, foundUnits.size());
       verify(unitOfMeasureRepository, times(1)).findAll();
    }

    @Test
    public void getUnitOfMeasureById() {
        Optional<UnitOfMeasure> optionalUnitOfMeasure = Optional.of(unitOfMeasureFromRepository);

        when(unitOfMeasureRepository.findById(UNIT_ID)).thenReturn(optionalUnitOfMeasure);
        when(unitOfMeasureToUnitOfMeasureCommand.convert(any())).thenReturn(unitOfMeasureCommand);

        UnitOfMeasureCommand foundUnitOfMeasure = unitOfMeasureService.getUnitOfMeasureById(UNIT_ID);

        assertNotNull(foundUnitOfMeasure);
        verify(unitOfMeasureRepository, times(1)).findById(UNIT_ID);
        verify(unitOfMeasureToUnitOfMeasureCommand, times(1)).convert(any());
    }
}