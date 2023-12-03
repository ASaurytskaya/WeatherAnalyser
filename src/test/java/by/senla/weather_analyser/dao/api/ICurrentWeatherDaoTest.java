package by.senla.weather_analyser.dao.api;

import by.senla.weather_analyser.dao.entity.CurrentWeatherEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
@SpringBootTest
public class ICurrentWeatherDaoTest {

    @Autowired
    private ICurrentWeatherDao currentWeatherDao;

    @Test
    @Transactional
    public void testExistsByLastUpdated() {
        LocalDateTime timestamp = LocalDateTime.parse("2023-12-02T18:15:00");

        boolean exists = currentWeatherDao.existsByLastUpdated(timestamp);

        assertThat(exists).isTrue();
    }

    @Test
    @Transactional
    public void testExistsByLastUpdated2() {
        LocalDateTime timestamp = LocalDateTime.now();

        boolean exists = currentWeatherDao.existsByLastUpdated(timestamp);

        assertThat(exists).isFalse();
    }

    @Test
    @Transactional
    public void testFindMostRecent() {

        Page<CurrentWeatherEntity> page = currentWeatherDao.findMostRecent(PageRequest.of(0, 1));

        assertThat(page).isNotNull();
        List<CurrentWeatherEntity> content = page.getContent();
        assertThat(content).isNotEmpty();
        assertThat(content.size()).isEqualTo(1);

        CurrentWeatherEntity entity = content.get(0);
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(UUID.fromString("0d3b93ea-0c69-41aa-a954-b1d7019b196d"));
        assertThat(entity.getTimestamp()).isEqualTo(LocalDateTime.parse("2023-12-02T19:27:28.310019"));
        assertThat(entity.getLastUpdated()).isEqualTo(LocalDateTime.parse("2023-12-02T18:15:00"));
        assertThat(entity.getTemperature()).isEqualTo(-7);
        assertThat(entity.getWindSpeed()).isEqualTo(4000);
        assertThat(entity.getCondition()).isEqualTo("Freezing fog");
        assertThat(entity.getPressure()).isEqualTo(1010);
        assertThat(entity.getHumidity()).isEqualTo( (short) 98);
        assertThat(entity.getLocation()).isEqualTo("Minsk");

    }

    @Test
    @Transactional
    public void testSave() {

        LocalDateTime timestamp = LocalDateTime.now();
        float temperature = 1;
        int  windSpeed = 1000;
        String condition = "Light Rain";
        int pressure = 988;
        short humidity = 100;
        String location = "Minsk";

        CurrentWeatherEntity entity = new CurrentWeatherEntity.Builder()
                .setId(UUID.randomUUID())
                .setTimestamp(timestamp)
                .setLastUpdated(timestamp)
                .setTemperature(temperature)
                .setWindSpeed(windSpeed)
                .setCondition(condition)
                .setPressure(pressure)
                .setHumidity(humidity)
                .setLocation(location)
                .build();

        CurrentWeatherEntity savedEntity = currentWeatherDao.saveAndFlush(entity);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isNotNull();
        assertThat(savedEntity.getTimestamp()).isEqualTo(timestamp);
        assertThat(savedEntity.getLastUpdated()).isEqualTo(timestamp);
        assertThat(savedEntity.getTemperature()).isEqualTo(temperature);
        assertThat(savedEntity.getWindSpeed()).isEqualTo(windSpeed);
        assertThat(savedEntity.getCondition()).isEqualTo(condition);
        assertThat(savedEntity.getPressure()).isEqualTo(pressure);
        assertThat(savedEntity.getHumidity()).isEqualTo(humidity);
        assertThat(savedEntity.getLocation()).isEqualTo(location);
    }
}
