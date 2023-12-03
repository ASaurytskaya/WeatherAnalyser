package by.senla.weather_analyser.controller;

import by.senla.weather_analyser.core.dto.AverageWeatherCondDto;
import by.senla.weather_analyser.core.dto.CurrentWeatherDto;
import by.senla.weather_analyser.service.api.ICurrentWeatherService;
import by.senla.weather_analyser.service.api.IHistoryWeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/weather")
public class WeatherController {
    private final ICurrentWeatherService currentWeatherService;
    private final IHistoryWeatherService historyWeatherService;

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    public WeatherController(ICurrentWeatherService currentWeatherService, IHistoryWeatherService historyWeatherService) {
        this.currentWeatherService = currentWeatherService;
        this.historyWeatherService = historyWeatherService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getCurrentWeather() {

        return new ResponseEntity<>(currentWeatherService.getLast(), HttpStatus.OK);
    }

    @GetMapping(value = "/history", produces = "application/json")
    public ResponseEntity<?> getHistoryWeather(@RequestParam LocalDate from, @RequestParam LocalDate to) {

        return new ResponseEntity<>(historyWeatherService.getAverage(from, to), HttpStatus.OK);
    }


}
