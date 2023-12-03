package by.senla.weather_analyser.dao.api;

import by.senla.weather_analyser.dao.entity.HistoryWeatherEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
@SpringBootTest
public class IHistoryWeatherDaoTest {

    @Autowired
    private IHistoryWeatherDao historyWeatherDao;

    @Test
    @Transactional
    public void testExistsByDate() {
        LocalDate date = LocalDate.now().plusDays(1);

        boolean exists = historyWeatherDao.existsByDate(date);

        assertThat(exists).isFalse();
    }

    @Test
    @Transactional
    public void testFindEntitiesByDateRange() {

        LocalDate startDate = LocalDate.parse("2023-11-25");
        LocalDate endDate =LocalDate.parse("2023-11-29");
        Page<HistoryWeatherEntity> page = historyWeatherDao.findEntitiesByDateRange(startDate, endDate, null);

        assertThat(page).isNotNull();
        List<HistoryWeatherEntity> content = page.stream().toList();
        assertThat(content).isNotEmpty();
        assertThat(content.size()).isEqualTo(5);
        for (HistoryWeatherEntity entity : content) {
            assertThat(entity.getDate()).isAfterOrEqualTo(startDate);
            assertThat(entity.getDate()).isBeforeOrEqualTo(endDate);
        }
    }

    @Test
    @Transactional
    public void testFindEntitiesByDateRange2() {

        LocalDate startDate = LocalDate.parse("2022-11-25");
        LocalDate endDate =LocalDate.parse("2022-11-29");
        Page<HistoryWeatherEntity> page = historyWeatherDao.findEntitiesByDateRange(startDate, endDate, null);

        assertThat(page).isNotNull();
        List<HistoryWeatherEntity> content = page.stream().toList();
        assertThat(content).isEmpty();
    }

    @Test
    @Transactional
    public void testSave() {
        LocalDate date = LocalDate.now();
        float temperature = 1;
        int  windSpeed = 1000;
        String condition = "Light Rain";
        int pressure = 988;
        short humidity = 100;
        String location = "Minsk";

        HistoryWeatherEntity entity = new HistoryWeatherEntity.Builder()
                .setId(UUID.randomUUID())
                .setDate(date)
                .setAvgTemp(temperature)
                .setAvgWindSpeed(windSpeed)
                .setCondition(condition)
                .setAvgPressure(pressure)
                .setAvgHumidity(humidity)
                .setLocation(location)
                .build();

        HistoryWeatherEntity savedEntity = historyWeatherDao.saveAndFlush(entity);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isNotNull();
        assertThat(savedEntity.getDate()).isEqualTo(date);
        assertThat(savedEntity.getAvgTemp()).isEqualTo(temperature);
        assertThat(savedEntity.getAvgWindSpeed()).isEqualTo(windSpeed);
        assertThat(savedEntity.getCondition()).isEqualTo(condition);
        assertThat(savedEntity.getAvgPressure()).isEqualTo(pressure);
        assertThat(savedEntity.getAvgHumidity()).isEqualTo(humidity);
        assertThat(savedEntity.getLocation()).isEqualTo(location);
    }
}
