package com.example.kayakquest.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.kayakquest.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WeatherFragment extends Fragment
{
    private TextView tvWeather;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        tvWeather = view.findViewById(R.id.tv_weather);
        fetchWeather("San Francisco");
        return view;
    }

    private void fetchWeather(String city)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi api = retrofit.create(WeatherApi.class);
        Call<WeatherResponse> call = api.getWeather(city, "YOUR_WEATHER_API_KEY", "metric");
        call.enqueue(new Callback<WeatherResponse>()
        {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    WeatherResponse weather = response.body();
                    String display = "City: " + weather.name + "\nTemp: " + weather.main.temp + "°C\nDescription: " + weather.weather[0].description;
                    tvWeather.setText(display);
                }
                else
                {
                    tvWeather.setText("Failed to fetch weather");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t)
            {
                tvWeather.setText("Error: " + t.getMessage());
            }
        });
    }

    public interface WeatherApi
    {
        @GET("weather")
        Call<WeatherResponse> getWeather(@Query("q") String city, @Query("appid") String apiKey, @Query("units") String units);
    }

    public static class WeatherResponse
    {
        public String name;
        public Main main;
        public Weather[] weather;

        public static class Main
        {
            public float temp;
        }

        public static class Weather
        {
            public String description;
        }
    }
}
