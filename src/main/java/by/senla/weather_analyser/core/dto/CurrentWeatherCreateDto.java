package by.senla.weather_analyser.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentWeatherCreateDto {

    private LocalDateTime lastUpdated;

    private float temperature;

    private int windSpeed;

    private int pressure;

    private short humidity;

    private String condition;

    private String location;

    @JsonProperty("location")
    public void setLocationDetails(LocationDetails locationDetails) {
        this.location = locationDetails.getName();
    }

    @JsonProperty("current")
    public void setCurrentDetails(CurrentDetails currentDetails) {
        this.lastUpdated = currentDetails.getLastUpdated();
        this.temperature = currentDetails.getTemperature();
        this.windSpeed = currentDetails.getWindSpeed();
        this.condition = currentDetails.getCondition().getText();
        this.pressure = currentDetails.getPressure();;
        this.humidity = currentDetails.getHumidity();
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

    private static class LocationDetails {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class CurrentDetails {

        private LocalDateTime lastUpdated;

        private float temperature;

        private int windSpeed;

        private int pressure;

        private short humidity;

        private ConditionDetails condition;

        public LocalDateTime getLastUpdated() {
            return lastUpdated;
        }

        @JsonProperty("last_updated")
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        public void setLastUpdated(LocalDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public float getTemperature() {
            return temperature;
        }

        @JsonProperty("temp_c")
        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }

        public int getWindSpeed() {
            return windSpeed;
        }

        @JsonProperty("wind_kph")
        public void setWindSpeed(int windSpeed) {
            this.windSpeed = windSpeed * 1000;
        }

        public int getPressure() {
            return pressure;
        }

        @JsonProperty("pressure_mb")
        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public short getHumidity() {
            return humidity;
        }

        @JsonProperty("humidity")
        public void setHumidity(short humidity) {
            this.humidity = humidity;
        }

        public ConditionDetails getCondition() {
            return condition;
        }

        @JsonProperty("condition")
        public void setCondition(ConditionDetails condition) {
            this.condition = condition;
        }
    }

    private static class ConditionDetails {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

        public LocalDateTimeDeserializer() {
            super(LocalDateTime.class);
        }

        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext context)
                throws IOException {
            String dateText = jsonParser.getText();
            return LocalDateTime.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
    }
}
