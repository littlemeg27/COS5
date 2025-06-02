package com.example.kayakquest.Operations;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OpenWeatherResponse
{
    @SerializedName("current")
    private Current current;
    @SerializedName("hourly")
    private List<Hourly> hourly;
    @SerializedName("timezone")
    private String timezone;

    public Current getCurrent()
    {
        return current;
    }

    public List<Hourly> getHourly()
    {
        return hourly;
    }

    public String getTimezone()
    {
        return timezone;
    }

    public static class Current
    {
        @SerializedName("temp")
        private double temperature;
        @SerializedName("humidity")
        private int humidity;
        @SerializedName("wind_speed")
        private double windSpeed;
        @SerializedName("pressure")
        private int pressure;
        @SerializedName("weather")
        private List<Weather> weather;

        public double getTemperature()
        {
            return temperature;
        }

        public int getHumidity()
        {
            return humidity;
        }

        public double getWindSpeed()
        {
            return windSpeed;
        }

        public int getPressure() {
            return pressure;
        }

        public List<Weather> getWeather()
        {
            return weather;
        }
    }

    public static class Weather
    {
        @SerializedName("description")
        private String description;

        public String getDescription()
        {
            return description;
        }
    }

    public static class Hourly
    {
        @SerializedName("dt")
        private long timestamp;
        @SerializedName("temp")
        private double temperature;
        @SerializedName("weather")
        private List<Weather> weather;

        public long getTimestamp()
        {
            return timestamp;
        }

        public double getTemperature()
        {
            return temperature;
        }

        public List<Weather> getWeather()
        {
            return weather;
        }
    }
}