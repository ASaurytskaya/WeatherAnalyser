package by.senla.weather_analyser.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public class CurrentWeatherDto {

    @JsonIgnore
    private UUID id;

    private LocalDateTime timestamp;

    @JsonProperty("last_updated")
    private LocalDateTime lastUpdated;

    private float temperature;

    @JsonProperty("wind_mph")
    private int windSpeed;

    @JsonProperty("pressure_mb")
    private int pressure;

    private short humidity;

    private String condition;

    private String location;

    public CurrentWeatherDto() {
    }

    private CurrentWeatherDto(Builder builder) {
        this.id = builder.id;
        this.timestamp = builder.timestamp;
        this.lastUpdated = builder.lastUpdated;
        this.temperature = builder.temperature;
        this.windSpeed = builder.windSpeed;
        this.pressure = builder.pressure;
        this.humidity = builder.humidity;
        this.condition = builder.condition;
        this.location = builder.location;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public float getTemperature() {
        return temperature;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public int getPressure() {
        return pressure;
    }

    public short getHumidity() {
        return humidity;
    }

    public String getCondition() {
        return condition;
    }

    public String getLocation() {
        return location;
    }

    public static class Builder {
        private UUID id;

        private LocalDateTime timestamp;

        private LocalDateTime lastUpdated;

        private float temperature;

        private int windSpeed;

        private int pressure;

        private short humidity;

        private String condition;

        private String location;

        public Builder setId(UUID id) {
            this.id = id;
            return this;
        }

        public Builder setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setLastUpdated(LocalDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public Builder setTemperature(float temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder setWindSpeed(int windSpeed) {
            this.windSpeed = windSpeed;
            return this;
        }

        public Builder setPressure(int pressure) {
            this.pressure = pressure;
            return this;
        }

        public Builder setHumidity(short humidity) {
            this.humidity = humidity;
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

        public CurrentWeatherDto build() {
            return new CurrentWeatherDto(this);
        }
    }
}
