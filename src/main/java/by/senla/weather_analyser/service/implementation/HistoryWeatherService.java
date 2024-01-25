package by.senla.weather_analyser.service.implementation;

import by.senla.weather_analyser.core.dto.AverageWeatherCondDto;
import by.senla.weather_analyser.core.dto.HistoryWeatherCreateDto;
import by.senla.weather_analyser.core.dto.HistoryWeatherDto;
import by.senla.weather_analyser.dao.api.IHistoryWeatherDao;
import by.senla.weather_analyser.dao.entity.HistoryWeatherEntity;
import by.senla.weather_analyser.service.api.IHistoryWeatherService;
import by.senla.weather_analyser.service.feign.IWeatherApiClient;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoryWeatherService implements IHistoryWeatherService {

    @Value("${app.location}")
    private String location;

    @Value("${app.api_key}")
    private String apiKey;

    private static final Logger logger = LoggerFactory.getLogger(CurrentWeatherService.class);

    private final IHistoryWeatherDao historyWeatherDao;
    private final IWeatherApiClient weatherApiClient;

    public HistoryWeatherService(IHistoryWeatherDao historyWeatherDao, IWeatherApiClient weatherApiClient) {
        this.historyWeatherDao = historyWeatherDao;
        this.weatherApiClient = weatherApiClient;
    }

    @Transactional
    @Override
    public AverageWeatherCondDto getAverage(LocalDate from, LocalDate to) {
        logger.info("Calculating average weather conditions for location {} from {} to {}", location, from, to);

        float sumTemp = 0;
        int sumWindSpeed = 0;
        int sumPressure = 0;
        int sumHumidity = 0;
        Map<String, Integer> condition = new HashMap<>();
        String location ;
        int count = 0;

        List<HistoryWeatherDto> list = getFromTo(from, to);

        if(list == null || list.isEmpty()) {
            logger.error("Illegal argument exception: list of <HistoryWeatherDto> from {} to {} cannot be null", from, to);
            throw new RuntimeException();
        }

        location = list.get(0).getLocation();
        for (HistoryWeatherDto weatherDto : list) {
            if(!location.equals(weatherDto.getLocation())) {
                continue;
            }
            count++;
            sumTemp += weatherDto.getAvgTemp();
            sumWindSpeed += weatherDto.getAvgWindSpeed();
            sumPressure += weatherDto.getAvgPressure();
            sumHumidity += weatherDto.getAvgHumidity();
            String conditionText = weatherDto.getCondition();
            if(condition.containsKey(conditionText)) {
                condition.put(conditionText, condition.get(conditionText) + 1);
            } else {
                condition.put(conditionText, 1);
            }
        }

        AverageWeatherCondDto dto = new AverageWeatherCondDto();
        dto.setAvgTemp(sumTemp / count);
        dto.setAvgWindSpeed(sumWindSpeed / count);

        Map.Entry<String, Integer> entryWithMaxCount = condition.entrySet().stream()
                                                            .max(Map.Entry.comparingByValue())
                                                            .orElse(null);
        dto.setCondition(entryWithMaxCount != null ? entryWithMaxCount.getKey() : null);

        dto.setAvgPressure(sumPressure / count);
        dto.setAvgHumidity(sumHumidity / count);
        dto.setLocation(location);

        logger.info("Average weather conditions calculated successfully for location {} from {} to {}", location, from, to);

        return dto;
    }

    @Transactional
    public List<HistoryWeatherDto> getFromTo(LocalDate from, LocalDate to) {
        logger.info("Fetching history weather data for location {} from {} to {}", location, from, to);

        List<HistoryWeatherDto> resultList = new ArrayList<>();
        List<HistoryWeatherEntity> entityList = historyWeatherDao
                .findEntitiesByDateRange(from, to, null)
                .stream().toList();


        if(entityList.isEmpty()){
            return from.datesUntil(to.plusDays(1))
                    .map((date -> loadHistoryWeather(location, date)))
                    .map(this::save)
                    .map(this::entityToDto)
                    .collect(Collectors.toList());
        }

        from.datesUntil(to.plusDays(1)).forEach(date -> {
            HistoryWeatherEntity existingEntity = entityList.stream()
                    .filter(entity -> entity.getDate().equals(date))
                    .findFirst()
                    .orElse(null);

            if (existingEntity != null) {
                resultList.add(entityToDto(existingEntity));
            } else {
                HistoryWeatherEntity newEntity = save(loadHistoryWeather(location, date));
                resultList.add(entityToDto(newEntity));
            }
        });

        logger.info("History weather data fetched successfully for location {} from {} to {}", location, from, to);

        return resultList;
    }

    @Transactional
    @Override
    public HistoryWeatherEntity save(HistoryWeatherCreateDto historyWeatherCreateDto) {

        if(historyWeatherCreateDto == null) {
            logger.error("Invalid argument: historyWeatherCreateDto is null");
            throw new RuntimeException("Invalid argument: historyWeatherCreateDto is null");
        }

        logger.info("Saving history weather data for location {} and date {}", location, historyWeatherCreateDto.getDate());

        HistoryWeatherEntity entity = new HistoryWeatherEntity.Builder()
                .setId(UUID.randomUUID())
                .setDate(historyWeatherCreateDto.getDate())
                .setAvgTemp(historyWeatherCreateDto.getAvgTemp())
                .setAvgWindSpeed(historyWeatherCreateDto.getAvgWindSpeed())
                .setCondition(historyWeatherCreateDto.getCondition())
                .setAvgPressure(historyWeatherCreateDto.getAvgPressure())
                .setAvgHumidity(historyWeatherCreateDto.getAvgHumidity())
                .setLocation(historyWeatherCreateDto.getLocation())
                .build();

        if(historyWeatherDao.existsByDate(historyWeatherCreateDto.getDate())) {
            logger.info("History weather data already existed for location {} and date {}", location, historyWeatherCreateDto.getDate());
            return entity;
        }

        logger.info("History weather data saved successfully for location {} and date {}", location, historyWeatherCreateDto.getDate());

        return historyWeatherDao.saveAndFlush(entity);
    }

    @Transactional
    @Scheduled(fixedRateString = "86400000")
    public void fetchDataAndSaveToDatabase() {
        try {
            logger.info("Scheduled task started: Fetching data from the API and saving to the database.");

            HistoryWeatherCreateDto createDto = loadHistoryWeather(location, LocalDate.now().minusDays(1));

            if (createDto != null) {

                save(createDto);
                logger.info("Data successfully fetched and saved to the database.");
            } else {
                logger.warn("Failed to fetch data from the API. Skipping database save.");
            }

        } catch (Exception e) {
            logger.error("Error in scheduled task: {}", e.getMessage(), e);
        }
    }

    private HistoryWeatherCreateDto loadHistoryWeather(String location, LocalDate date) {
        HistoryWeatherCreateDto createDto = null;
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        logger.info("Fetching history weather data from the API for location {} and date {}", location, formattedDate);

        try{
            createDto = weatherApiClient.getWeatherHistory(location, formattedDate, apiKey);
        }  catch (FeignException e) {
            handleFeignException(e);
        }
        logger.info("History weather data fetched successfully from the API for location {} and date {}", location, formattedDate);

        return createDto;
    }

    private HistoryWeatherDto entityToDto(HistoryWeatherEntity entity) {
        logger.debug("Converting HistoryWeatherEntity to HistoryWeatherDto: {}", entity);

        return new HistoryWeatherDto.Builder()
                        .setId(entity.getId())
                        .setDate(entity.getDate())
                        .setAvgTemp(entity.getAvgTemp())
                        .setAvgWindSpeed(entity.getAvgWindSpeed())
                        .setCondition(entity.getCondition())
                        .setAvgPressure(entity.getAvgPressure())
                        .setAvgHumidity(entity.getAvgHumidity())
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
