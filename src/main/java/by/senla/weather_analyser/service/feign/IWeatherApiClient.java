package by.senla.weather_analyser.service.feign;

import by.senla.weather_analyser.core.dto.CurrentWeatherCreateDto;
import by.senla.weather_analyser.core.dto.HistoryWeatherCreateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weatherApiClient", url = "https://weatherapi-com.p.rapidapi.com")
public interface IWeatherApiClient {

    @GetMapping("/current.json")
    CurrentWeatherCreateDto getCurrentWeather(
            @RequestParam String q,
            @RequestHeader("X-Rapidapi-Key") String apiKey,
            @RequestHeader("X-Rapidapi-Host") String apiHost
    );

    @GetMapping("/history.json")
    HistoryWeatherCreateDto getWeatherHistory(
            @RequestParam String q,
            @RequestParam String dt,
            @RequestHeader("X-Rapidapi-Key") String apiKey,
            @RequestHeader("X-Rapidapi-Host") String apiHost
    );
}
