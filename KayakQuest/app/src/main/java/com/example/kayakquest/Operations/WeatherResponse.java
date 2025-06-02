package com.example.kayakquest.Operations;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class WeatherResponse
{
    @SerializedName("location")
    private Location location;
    @SerializedName("current")
    private Current current;
    @SerializedName("forecast")
    private Map<String, ForecastDay> forecast;

    public Location getLocation()
    {
        return location;
    }

    public Current getCurrent()
    {
        return current;
    }

    public Map<String, ForecastDay> getForecast()
    {
        return forecast;
    }

    public static class Location
    {
        @SerializedName("name")
        private String name;

        public String getName()
        {
            return name;
        }
    }

    public static class Current {
        @SerializedName("temperature")
        private int temperature;
        @SerializedName("weather_descriptions")
        private List<String> weatherDescriptions;
        @SerializedName("humidity")
        private int humidity;
        @SerializedName("wind_speed")
        private int windSpeed;
        @SerializedName("pressure")
        private int pressure;

        public int getTemperature()
        {
            return temperature;
        }

        public List<String> getWeatherDescriptions() {
            return weatherDescriptions;
        }

        public int getHumidity() {
            return humidity;
        }

        public int getWindSpeed() {
            return windSpeed;
        }

        public int getPressure() {
            return pressure;
        }
    }

    public static class ForecastDay
    {
        @SerializedName("hourly")
        private List<Hourly> hourly;

        public List<Hourly> getHourly()
        {
            return hourly;
        }
    }

    public static class Hourly
    {
        @SerializedName("time")
        private String time;
        @SerializedName("temperature")
        private int temperature;
        @SerializedName("weather_descriptions")
        private List<String> weatherDescriptions;
        @SerializedName("humidity")
        private int humidity;

        public String getTime()
        {
            return time;
        }

        public int getTemperature()
        {
            return temperature;
        }

        public List<String> getWeatherDescriptions()
        {
            return weatherDescriptions;
        }

        public int getHumidity()
        {
            return humidity;
        }
    }
}
