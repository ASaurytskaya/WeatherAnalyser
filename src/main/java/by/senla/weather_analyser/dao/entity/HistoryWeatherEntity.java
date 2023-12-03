package by.senla.weather_analyser.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "history_weather")
public class HistoryWeatherEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "avg_temperature", nullable = false)
    private float avgTemp;

    @Column(name = "avg_wind_mph", nullable = false)
    private int avgWindSpeed;

    @Column(name = "avg_pressure_mb", nullable = false)
    private int avgPressure;

    @Column(name = "avg_humidity", nullable = false)
    private short avgHumidity;

    @Column(nullable = false)
    private String condition;

    @Column(nullable = false)
    private String location;

    public HistoryWeatherEntity(){}

    public HistoryWeatherEntity(Builder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.avgTemp = builder.avgTemp;
        this.avgWindSpeed = builder.avgWindSpeed;
        this.avgPressure = builder.avgPressure;
        this.avgHumidity = builder.avgHumidity;
        this.condition = builder.condition;
        this.location = builder.location;
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public float getAvgTemp() {
        return avgTemp;
    }

    public int getAvgWindSpeed() {
        return avgWindSpeed;
    }

    public int getAvgPressure() {
        return avgPressure;
    }

    public short getAvgHumidity() {
        return avgHumidity;
    }

    public String getCondition() {
        return condition;
    }

    public String getLocation() {
        return location;
    }

    public static class Builder {

        private UUID id;

        private LocalDate date;

        private float avgTemp;

        private int avgWindSpeed;

        private int avgPressure;

        private short avgHumidity;

        private String condition;

        private String location;

        public Builder setId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder setAvgTemp(float avgTemp) {
            this.avgTemp = avgTemp;
            return this;
        }

        public Builder setAvgWindSpeed(int avgWindSpeed) {
            this.avgWindSpeed = avgWindSpeed;
            return this;
        }

        public Builder setAvgPressure(int avgPressure) {
            this.avgPressure = avgPressure;
            return this;
        }

        public Builder setAvgHumidity(short avgHumidity) {
            this.avgHumidity = avgHumidity;
            return this;
        }

        public Builder setCondition(String condition) {
            this.condition = condition;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public HistoryWeatherEntity build() {
            return new HistoryWeatherEntity(this);
        }
    }

}
