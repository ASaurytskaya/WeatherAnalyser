package by.senla.weather_analyser.dao.api;

import by.senla.weather_analyser.dao.entity.CurrentWeatherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ICurrentWeatherDao extends JpaRepository<CurrentWeatherEntity, UUID> {
    boolean existsByLastUpdated(LocalDateTime timestamp);

    @Query("SELECT e FROM CurrentWeatherEntity e ORDER BY e.lastUpdated DESC")
    Page<CurrentWeatherEntity> findMostRecent(Pageable pageable);
}
