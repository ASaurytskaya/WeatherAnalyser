package by.senla.weather_analyser.service.implementation;


import by.senla.weather_analyser.core.dto.CurrentWeatherCreateDto;
import by.senla.weather_analyser.core.dto.CurrentWeatherDto;
import by.senla.weather_analyser.dao.api.ICurrentWeatherDao;
import by.senla.weather_analyser.dao.entity.CurrentWeatherEntity;
import by.senla.weather_analyser.service.api.ICurrentWeatherService;
import by.senla.weather_analyser.service.feign.IWeatherApiClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CurrentWeatherService implements ICurrentWeatherService {

    @Value("${app.location}")
    private String location;

    @Value("${app.api_key}")
    private String apiKey;

    private static final Logger logger = LoggerFactory.getLogger(CurrentWeatherService.class);

    private final ICurrentWeatherDao currentWeatherDao;
    private final IWeatherApiClient weatherApiClient;

    public CurrentWeatherService(ICurrentWeatherDao currentWeatherDao, IWeatherApiClient weatherApiClient) {
        this.currentWeatherDao = currentWeatherDao;
        this.weatherApiClient = weatherApiClient;
    }

    @Transactional()
    @Override
    public CurrentWeatherDto getLast() {
        logger.info("Fetching the last current weather data");

       Page<CurrentWeatherEntity> queryResult = currentWeatherDao.findMostRecent(PageRequest.of(0, 1));
       CurrentWeatherEntity entity = queryResult.stream().findFirst().orElse(null);

       if(entity == null) {
           logger.info("No existing data found. Fetching current weather data from the API.");
           entity = this.save(this.loadCurrentWeather(location));
       }

        logger.debug("Returning the last current weather data: {}", entity);

       return entityToDto(entity);
    }

    @Transactional
    @Override
    public CurrentWeatherEntity save(CurrentWeatherCreateDto currentWeatherCreateDto) {
        logger.info("Saving current weather data: {}", currentWeatherCreateDto);

        if(currentWeatherCreateDto == null) {
            logger.error("Invalid argument: currentWeatherCreateDto is null");
            throw new RuntimeException("Invalid argument: currentWeatherCreateDto is null");
        }

        CurrentWeatherEntity entity = new CurrentWeatherEntity.Builder()
                                                .setId(UUID.randomUUID())
                                                .setTimestamp(LocalDateTime.now())
                                                .setLastUpdated(currentWeatherCreateDto.getLastUpdated())
                                                .setTemperature(currentWeatherCreateDto.getTemperature())
                                                .setWindSpeed(currentWeatherCreateDto.getWindSpeed())
                                                .setCondition(currentWeatherCreateDto.getCondition())
                                                .setPressure(currentWeatherCreateDto.getPressure())
                                                .setHumidity(currentWeatherCreateDto.getHumidity())
                                                .setLocation(currentWeatherCreateDto.getLocation())
                                                .build();

        if(currentWeatherDao.existsByLastUpdated(currentWeatherCreateDto.getLastUpdated())) {
            logger.info("Current weather data already existed for location {} and date {}", location, currentWeatherCreateDto.getLastUpdated());
            return entity;
        }

        logger.debug("Current weather data saved successfully: {}", entity);
        return currentWeatherDao.saveAndFlush(entity);
    }

    @Transactional
    @Scheduled(fixedRateString = "${scheduled.task.fixedRate}")
    public void fetchDataAndSaveToDatabase() {
        try {
            logger.info("Scheduled task started: Fetching data from the API and saving to the database.");

            CurrentWeatherCreateDto currentWeatherCreateDto = loadCurrentWeather(location);

            if (currentWeatherCreateDto != null) {
                deleteAll();

                save(currentWeatherCreateDto);
                logger.info("Data successfully fetched and saved to the database.");
            } else {
                logger.warn("Failed to fetch data from the API. Skipping database save.");
            }

        } catch (Exception e) {
            logger.error("Error in scheduled task: {}", e.getMessage(), e);
        }
    }

    public void deleteAll() {
        logger.info("Deleting all current weather data");

        currentWeatherDao.deleteAll();

        logger.info("All current weather data deleted");
    }

    private CurrentWeatherCreateDto loadCurrentWeather(String location) {
        logger.info("Fetching current weather data from the API for location: {}", location);

        CurrentWeatherCreateDto createDto = null;

        try{
            createDto = weatherApiClient.getCurrentWeather(location, apiKey);
            logger.debug("Fetched current weather data from the API: {}", createDto);
        } catch (FeignException e) {
            handleFeignException(e);
        }
        return createDto;
    }

    private CurrentWeatherDto entityToDto(CurrentWeatherEntity entity) {
        logger.debug("Converting CurrentWeatherEntity to CurrentWeatherDto: {}", entity);
        return new CurrentWeatherDto.Builder()
                        .setId(entity.getId())
                        .setTimestamp(entity.getTimestamp())
                        .setLastUpdated(entity.getLastUpdated())
                        .setTemperature(entity.getTemperature())
                        .setWindSpeed(entity.getWindSpeed())
                        .setCondition(entity.getCondition())
                        .setPressure(entity.getPressure())
                        .setHumidity(entity.getHumidity())
                        .setLocation(entity.getLocation())
                        .build();
    }

    private void handleFeignException(FeignException e) {
        int statusCode = e.status();
        String reason = e.getMessage();

        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            logger.warn("API returned 404 Not Found. The requested resource was not found.");
        } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
            logger.error("API returned 401 Unauthorized. Check your API key and authentication.");
        } else {
            logger.error("Error calling the API. Status code: {}, Reason: {}", statusCode, reason);
        }

    }
}
