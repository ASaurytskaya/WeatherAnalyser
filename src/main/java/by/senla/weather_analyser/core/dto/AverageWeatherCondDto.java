package by.senla.weather_analyser.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AverageWeatherCondDto {

    private float avgTemp;

    @JsonProperty("wind_speed_mph")
    private int avgWindSpeed;

    @JsonProperty("average_pressure_mb")
    private int avgPressure;

    @JsonProperty("average_humidity")
    private int avgHumidity;

    private String condition;

    private String location;

    public float getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(float avgTemp) {
        this.avgTemp = avgTemp;
    }

    public int getAvgWindSpeed() {
        return avgWindSpeed;
    }

    public void setAvgWindSpeed(int avgWindSpeed) {
        this.avgWindSpeed = avgWindSpeed;
    }

    public int getAvgPressure() {
        return avgPressure;
    }

    public void setAvgPressure(int avgPressure) {
        this.avgPressure = avgPressure;
    }

    public int getAvgHumidity() {
        return avgHumidity;
    }

    public void setAvgHumidity(int avgHumidity) {
        this.avgHumidity = avgHumidity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
