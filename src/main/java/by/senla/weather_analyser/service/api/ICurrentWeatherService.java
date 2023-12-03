package by.senla.weather_analyser.service.api;

import by.senla.weather_analyser.core.dto.CurrentWeatherCreateDto;
import by.senla.weather_analyser.core.dto.CurrentWeatherDto;
import by.senla.weather_analyser.dao.entity.CurrentWeatherEntity;

public interface ICurrentWeatherService extends IWeatherService<CurrentWeatherCreateDto, CurrentWeatherEntity> {
    CurrentWeatherDto getLast();

    void deleteAll();


}
