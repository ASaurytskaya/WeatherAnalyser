package by.senla.weather_analyser.service.implementation;
import by.senla.weather_analyser.core.dto.CurrentWeatherCreateDto;
import by.senla.weather_analyser.core.dto.CurrentWeatherDto;
import by.senla.weather_analyser.dao.api.ICurrentWeatherDao;
import by.senla.weather_analyser.dao.entity.CurrentWeatherEntity;
import by.senla.weather_analyser.service.feign.IWeatherApiClient;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@SpringBootTest
public class CurrentWeatherServiceTest {

    @MockBean
    private ICurrentWeatherDao currentWeatherDao;

    @Mock
    private IWeatherApiClient weatherApiClient;

    @Autowired
    @InjectMocks
    private CurrentWeatherService currentWeatherService;


    @Test
    public void testGetLast() {
        CurrentWeatherEntity mockedEntity = new CurrentWeatherEntity();
        Mockito.when(currentWeatherDao.findMostRecent(Mockito.any())).thenReturn(new PageImpl<>(Collections.singletonList(mockedEntity)));

        CurrentWeatherCreateDto mockedDto = new CurrentWeatherCreateDto();
        Mockito.when(weatherApiClient.getCurrentWeather(Mockito.anyString(), Mockito.anyString())).thenReturn(mockedDto);

        CurrentWeatherDto result = currentWeatherService.getLast();

        assertNotNull(result);

        Mockito.verify(currentWeatherDao, Mockito.times(1)).findMostRecent(Mockito.any());
    }

    @Test
    public void testSave() {

        Mockito.when(currentWeatherDao.existsByLastUpdated(Mockito.any())).thenReturn(false);
        Mockito.when(currentWeatherDao.saveAndFlush(Mockito.any())).thenReturn(new CurrentWeatherEntity());

        CurrentWeatherEntity result = currentWeatherService.save(new CurrentWeatherCreateDto());

        assertNotNull(result);

        Mockito.verify(currentWeatherDao, Mockito.times(1)).existsByLastUpdated(Mockito.any());
        Mockito.verify(currentWeatherDao, Mockito.times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    public void testExceptionHandling() {
        Mockito.when(weatherApiClient.getCurrentWeather(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(FeignException.class);

        assertThrows(Exception.class, () -> currentWeatherService.getLast());
    }

    @Test
    public void testSaveNotCalledWhenDataExists() {
        Mockito.when(currentWeatherDao.existsByLastUpdated(Mockito.any())).thenReturn(true);

        currentWeatherService.save(new CurrentWeatherCreateDto());

        Mockito.verify(currentWeatherDao, Mockito.never()).saveAndFlush(Mockito.any());
    }

}
