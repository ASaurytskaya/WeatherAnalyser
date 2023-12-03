package by.senla.weather_analyser.service.api;

import by.senla.weather_analyser.core.dto.AverageWeatherCondDto;
import by.senla.weather_analyser.core.dto.HistoryWeatherCreateDto;
import by.senla.weather_analyser.dao.entity.HistoryWeatherEntity;

import java.time.LocalDate;

public interface IHistoryWeatherService extends IWeatherService<HistoryWeatherCreateDto, HistoryWeatherEntity> {
    AverageWeatherCondDto getAverage(LocalDate from, LocalDate to);
}
