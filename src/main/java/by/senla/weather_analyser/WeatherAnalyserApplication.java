package by.senla.weather_analyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories
@EnableTransactionManagement
@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class WeatherAnalyserApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherAnalyserApplication.class, args);
	}

}
