package by.senla.weather_analyser.service.implementation;

import by.senla.weather_analyser.core.dto.HistoryWeatherCreateDto;
import by.senla.weather_analyser.core.dto.HistoryWeatherDto;
import by.senla.weather_analyser.dao.api.IHistoryWeatherDao;
import by.senla.weather_analyser.dao.entity.HistoryWeatherEntity;
import by.senla.weather_analyser.service.feign.IWeatherApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@SpringBootTest
class HistoryWeatherServiceTest {
    private static final String API_KEY = "c099b7f519msh246359e17ca184ap1b00b4jsn7687b9527026";
    private static final String API_HOST = "weatherapi-com.p.rapidapi.com";

    @Mock
    private IWeatherApiClient weatherApiClient;

    @MockBean
    private IHistoryWeatherDao historyWeatherDao;

    @InjectMocks
    @Autowired
    private HistoryWeatherService historyWeatherService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFromTo() {

        LocalDate from = LocalDate.of(2023, 11, 25);
        LocalDate to = LocalDate.of(2023, 11, 29);

        List<HistoryWeatherEntity> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(createMockEntity(from.plusDays(i)));
        }

        Mockito.when(historyWeatherDao.findEntitiesByDateRange(from, to, null))
                .thenReturn(new PageImpl<>(list));

        List<HistoryWeatherDto> result = historyWeatherService.getFromTo(from, to);

        assertThat(result).isNotNull().hasSize(5);
        for (HistoryWeatherDto dto : result) {
            assertThat(dto.getDate()).isAfterOrEqualTo(from);
            assertThat(dto.getDate()).isBeforeOrEqualTo(to);
        }
    }


    @Transactional
    @Test
    void testSave() {
        LocalDate date = LocalDate.now();

        Mockito.when(historyWeatherDao.existsByDate(date))
                .thenReturn(false);
        Mockito.when(historyWeatherDao.saveAndFlush(createMockEntity(date)))
                .thenReturn(createMockEntity(date));

        historyWeatherService.save(createMockCreateDto(date));
        Mockito.verify(historyWeatherDao, Mockito.times(1)).existsByDate(date);
        Mockito.verify(historyWeatherDao, Mockito.times(1)).saveAndFlush(any(HistoryWeatherEntity.class));
    }

    @Transactional
    @Test
    void testSave2() {
        LocalDate date = LocalDate.of(2023, 11, 25);
        Mockito.when(historyWeatherDao.existsByDate(date))
                .thenReturn(true);
        Mockito.when(historyWeatherDao.saveAndFlush(createMockEntity(date)))
                .thenReturn(createMockEntity(date));

        historyWeatherService.save(createMockCreateDto(date));

        Mockito.verify(historyWeatherDao, Mockito.times(1)).existsByDate(date);
        Mockito.verify(historyWeatherDao, Mockito.times(0)).saveAndFlush(any(HistoryWeatherEntity.class));
    }

    @Transactional
    @Test
    void testFetchDataAndSaveToDatabase() {
        LocalDate currentDate = LocalDate.now().minusDays(1);
        Mockito.when(weatherApiClient.getWeatherHistory("Minsk", currentDate.toString(), API_KEY, API_HOST))
                .thenReturn(createMockCreateDto(currentDate));

        historyWeatherService.fetchDataAndSaveToDatabase();

        Mockito.verify(historyWeatherDao, Mockito.times(1)).saveAndFlush(any(HistoryWeatherEntity.class));
    }

    private HistoryWeatherCreateDto createMockCreateDto(LocalDate date) {
        HistoryWeatherCreateDto dto = new HistoryWeatherCreateDto();
        dto.setDate(date);
        dto.setAvgTemp(-1.9f);
        dto.setAvgWindSpeed(20000);
        dto.setCondition("Moderate snow");
        dto.setAvgPressure(994);
        dto.setAvgHumidity((short) 92);
        dto.setLocation("Minsk");

        return dto;
    }

    private HistoryWeatherEntity createMockEntity(LocalDate date) {
        return new HistoryWeatherEntity.Builder()
                .setId(UUID.randomUUID())
                .setDate(date)
                .setAvgTemp(-1.9f)
                .setAvgWindSpeed(20000)
                .setCondition("Moderate snow")
                .setAvgPressure(994)
                .setAvgHumidity((short) 92)
                .setLocation("Minsk")
                .build();
    }
}
