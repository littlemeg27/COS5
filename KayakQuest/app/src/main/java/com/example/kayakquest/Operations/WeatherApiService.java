package com.example.kayakquest.Operations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService
{
    @GET("data/3.0/onecall")
    Call<OpenWeatherResponse> getWeather(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("exclude") String exclude
    );
}