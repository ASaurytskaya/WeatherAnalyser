package by.senla.weather_analyser.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryWeatherCreateDto {
    private String location;

    private LocalDate date;

    private float avgTemp;

    private int avgWindSpeed;

    private int avgPressure;

    private short avgHumidity;

    private String condition;

    @JsonProperty("location")
    public void setLocationDetails(LocationDetails locationDetails) {
        this.location = locationDetails.getName();
    }

    @JsonProperty("forecast")
    public void setForecast(ForecastDetails forecastDetails) {
        this.date = forecastDetails.getForecastDayDetails().get(0).getDate();
        DayDetails dayDetails = forecastDetails.getForecastDayDetails().get(0).dayDetails;
        this.avgTemp = dayDetails.getAvgTemperature();
        this.avgWindSpeed = dayDetails.getAvgWindSpeed();
        this.avgHumidity = dayDetails.getAvgHumidity();
        this.condition = dayDetails.getCondition().getText();

        calculateAveragePressure(forecastDetails);
    }

    private void calculateAveragePressure(ForecastDetails forecastDetails) {
        int totalPressure = 0;
        int count = 0;

        List<HourDetails> hours = forecastDetails.getForecastDayDetails().get(0).getHourDetailsList();
        if (hours != null) {
            for (HourDetails hour : hours) {
                totalPressure += hour.getPressureMb();
                count++;
            }
        }

        this.avgPressure = totalPressure / count;

    }

    public String getLocation() {
        return location;
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

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAvgTemp(float avgTemp) {
        this.avgTemp = avgTemp;
    }

    public void setAvgWindSpeed(int avgWindSpeed) {
        this.avgWindSpeed = avgWindSpeed;
    }

    public void setAvgPressure(int avgPressure) {
        this.avgPressure = avgPressure;
    }

    public void setAvgHumidity(short avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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

    private static class ForecastDetails {
        private List<ForecastDayDetails> forecastDayDetails;

        public List<ForecastDayDetails> getForecastDayDetails() {
            return forecastDayDetails;
        }

        @JsonProperty("forecastday")
        public void setForecastDayDetails(List<ForecastDayDetails> forecastDayDetails) {
            this.forecastDayDetails = forecastDayDetails;
        }
    }

    private static class ForecastDayDetails {
        private LocalDate date;

        private DayDetails dayDetails;

        private List<HourDetails> hourDetailsList;

        public LocalDate getDate() {
            return date;
        }

        @JsonProperty("date")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        public void setDate(LocalDate date) {
            this.date = date;
        }


        @JsonProperty("hour")
        public void setHourDetailsList(List<HourDetails> hourDetailsList) {
            this.hourDetailsList = hourDetailsList;
        }

        public List<HourDetails> getHourDetailsList() {
            return hourDetailsList;
        }

        @JsonProperty("day")
        public void setDayDetails(DayDetails dayDetails) {
            this.dayDetails = dayDetails;
        }

        public DayDetails getDayDetails() {
            return dayDetails;
        }
    }

    private static class DayDetails {

        private float avgTemperature;

        private int avgWindSpeed;

        private short avgHumidity;
        private ConditionDetails condition;

        public float getAvgTemperature() {
            return avgTemperature;
        }

        @JsonProperty("avgtemp_c")
        public void setAvgTemperature(float avgTemperature) {
            this.avgTemperature = avgTemperature;
        }

        public int getAvgWindSpeed() {
            return avgWindSpeed;
        }

        @JsonProperty("maxwind_kph")
        public void setAvgWindSpeed(int avgWindSpeed) {
            this.avgWindSpeed = avgWindSpeed * 1000;
        }

        public short getAvgHumidity() {
            return avgHumidity;
        }

        @JsonProperty("avghumidity")
        public void setAvgHumidity(short avgHumidity) {
            this.avgHumidity = avgHumidity;
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

    private static class HourDetails {
        private double pressure;

        public double getPressureMb() {
            return pressure;
        }

        @JsonProperty("pressure_mb")
        public void setPressure(double pressureMb) {
            this.pressure = pressureMb;
        }
    }

    public static class LocalDateDeserializer extends StdDeserializer<LocalDate> {

        public LocalDateDeserializer() {
            super(LocalDate.class);
        }

        @Override
        public LocalDate deserialize(JsonParser jsonParser, DeserializationContext context)
                throws IOException {
            String dateText = jsonParser.getText();
            return LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }

}

