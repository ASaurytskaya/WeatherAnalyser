package by.senla.weather_analyser.service.feign;

import by.senla.weather_analyser.core.dto.CurrentWeatherCreateDto;
import by.senla.weather_analyser.core.dto.HistoryWeatherCreateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weatherApiClient", url = "http://api.weatherapi.com/v1")
public interface IWeatherApiClient {

    @GetMapping("/current.json")
    CurrentWeatherCreateDto getCurrentWeather(
            @RequestParam String q,
            @RequestParam String key
    );

    @GetMapping("/history.json")
    HistoryWeatherCreateDto getWeatherHistory(
            @RequestParam String q,
            @RequestParam String dt,
            @RequestParam String key
    );
}
