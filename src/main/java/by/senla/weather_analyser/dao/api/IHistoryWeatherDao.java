package by.senla.weather_analyser.dao.api;

import by.senla.weather_analyser.dao.entity.HistoryWeatherEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface IHistoryWeatherDao extends JpaRepository<HistoryWeatherEntity, UUID> {

    boolean existsByDate(LocalDate date);

    @Query("SELECT e FROM HistoryWeatherEntity e WHERE e.date BETWEEN :startDate AND :endDate")
    Page<HistoryWeatherEntity> findEntitiesByDateRange(@Param("startDate") LocalDate from, @Param("endDate") LocalDate to, Pageable pageable);
}
