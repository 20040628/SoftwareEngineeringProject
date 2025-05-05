package group6.demo.service.impl;

import group6.demo.dto.AvailableScooterDTO;
import group6.demo.dto.ScooterAddDTO;
import group6.demo.entity.Order;
import group6.demo.entity.Scooter;
import group6.demo.entity.Store;
import group6.demo.repository.OrderRepository;
import group6.demo.repository.ScooterRepository;
import group6.demo.repository.StoreRepository;
import group6.demo.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ScooterServiceImplTest {

    @InjectMocks
    private ScooterServiceImlp scooterService;

    @Mock
    private ScooterRepository scooterRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StoreRepository storeRepository;

    private ScooterAddDTO validDto;

    @BeforeEach
    void setUp() {
        validDto = new ScooterAddDTO();
        validDto.setPriceHour(new BigDecimal("3.00"));
        validDto.setPriceFourHour(new BigDecimal("10.00"));
        validDto.setPriceDay(new BigDecimal("20.00"));
        validDto.setPriceWeek(new BigDecimal("80.00"));
        validDto.setBattery(new BigDecimal("60.00"));
        validDto.setSpeed(new BigDecimal("25.00"));
        validDto.setStoreId(1L);
    }

    @Test
    void addScooter_validInput_shouldReturnSavedScooter() {
        Store mockStore = new Store();
        when(storeRepository.findById(1L)).thenReturn(Optional.of(mockStore));
        when(scooterRepository.save(any(Scooter.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Scooter result = scooterService.addScooter(validDto);

        assertNotNull(result);
        assertEquals(validDto.getPriceHour(), result.getPriceHour());
        assertEquals(1, result.getStatus());
        verify(scooterRepository, times(1)).save(any(Scooter.class));
    }

    @Test
    void getAllScooters_shouldReturnList() {
        List<Scooter> scooters = Arrays.asList(new Scooter(), new Scooter());
        when(scooterRepository.findAll()).thenReturn(scooters);

        List<Scooter> result = scooterService.getAllScooters();

        assertEquals(2, result.size());
        verify(scooterRepository).findAll();
    }

    @Test
    void getScootersAvailable_shouldReturnAvailableScooters() throws Exception {
        // Setup input
        AvailableScooterDTO dto = new AvailableScooterDTO();
        dto.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis() + 3600000)));
        dto.setHireType("HOUR");

        // Create scooter
        Scooter scooter = new Scooter();
        scooter.setId(1L);
        scooter.setBattery(new BigDecimal("50.00"));

        // Mock
        when(scooterRepository.findByStoreId(1L)).thenReturn(Collections.singletonList(scooter));
        when(orderRepository.findConflictingOrders(eq(1L), any(), any())).thenReturn(Collections.emptyList());

        List<Scooter> result = scooterService.getScootersAvailable(dto, 1L);

        assertEquals(1, result.size());
    }

    @Test
    void getScooterByStoreId_shouldReturnScooters() {
        List<Scooter> list = List.of(new Scooter());
        when(scooterRepository.findByStoreId(1L)).thenReturn(list);

        List<Scooter> result = scooterService.getScooterByStoreId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void getScooterById_existingId_shouldReturnScooter() {
        Scooter scooter = new Scooter();
        when(scooterRepository.findById(1L)).thenReturn(Optional.of(scooter));

        Optional<Scooter> result = scooterService.getScooterById(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void changeScooterStatus_shouldToggleStatus() {
        Scooter scooter = new Scooter();
        scooter.setId(1L);
        scooter.setStatus(1); // 当前为可用状态

        when(scooterRepository.findById(1L)).thenReturn(Optional.of(scooter));
        when(scooterRepository.save(any(Scooter.class))).thenAnswer(i -> i.getArgument(0));

        Optional<Scooter> result = scooterService.changeScooterStatus(1L);

        assertTrue(result.isPresent());
        assertEquals(0, result.get().getStatus());

        // 再切换回来
        scooter.setStatus(0);
        result = scooterService.changeScooterStatus(1L);
        assertEquals(1, result.get().getStatus());
    }
}
