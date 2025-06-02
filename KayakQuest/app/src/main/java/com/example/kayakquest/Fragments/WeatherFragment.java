package com.example.kayakquest.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kayakquest.BuildConfig;
import com.example.kayakquest.Operations.OpenWeatherResponse;
import com.example.kayakquest.Operations.SelectedPinViewModel;
import com.example.kayakquest.Operations.WeatherApiService;
import com.example.kayakquest.R;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherFragment extends Fragment
{
    private TextView currentLocation, currentTemp, currentDescription, currentHumidity, currentWind, currentPressure;
    private RecyclerView hourlyForecastRecycler;
    private ProgressBar progressBar;
    private HourlyForecastAdapter adapter;
    private WeatherApiService apiService;
    private SelectedPinViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        currentLocation = view.findViewById(R.id.current_location);
        currentTemp = view.findViewById(R.id.current_temp);
        currentDescription = view.findViewById(R.id.current_description);
        currentHumidity = view.findViewById(R.id.current_humidity);
        currentWind = view.findViewById(R.id.current_wind);
        currentPressure = view.findViewById(R.id.current_pressure);
        hourlyForecastRecycler = view.findViewById(R.id.hourly_forecast_recycler);
        progressBar = view.findViewById(R.id.progress_bar);

        hourlyForecastRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new HourlyForecastAdapter(new ArrayList<>());
        hourlyForecastRecycler.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(WeatherApiService.class);

        viewModel = new ViewModelProvider(requireActivity()).get(SelectedPinViewModel.class);
        viewModel.getSelectedPin().observe(getViewLifecycleOwner(), latLng ->
        {
            if (latLng != null)
            {
                fetchWeatherData(latLng.latitude, latLng.longitude);
                Log.d("WeatherFragment", "Fetching weather for: " + latLng.latitude + "," + latLng.longitude);
            }
        });

        fetchWeatherData(35.227085, -80.843124);
        return view;
    }

    private void fetchWeatherData(double lat, double lon)
    {
        progressBar.setVisibility(View.VISIBLE);
        Call<OpenWeatherResponse> call = apiService.getWeather(
                lat,
                lon,
                BuildConfig.OPENWEATHER_API_KEY,
                "imperial",
                "minutely,daily,alerts"
        );

        call.enqueue(new Callback<OpenWeatherResponse>()
        {
            @Override
            public void onResponse(@NonNull Call<OpenWeatherResponse> call, @NonNull Response<OpenWeatherResponse> response)
            {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null)
                {
                    OpenWeatherResponse weather = response.body();
                    updateCurrentWeather(weather);
                    updateHourlyForecast(weather);
                    Log.d("WeatherFragment", "Weather fetched for timezone: " + weather.getTimezone());
                }
                else
                {
                    String errorBody = "";
                    try
                    {
                        if (response.errorBody() != null)
                        {
                            errorBody = response.errorBody().string();
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("WeatherFragment", "Error reading error body: " + e.getMessage());
                    }
                    Log.e("WeatherFragment", "Response error: " + response.code() + " " + response.message() + " " + errorBody);
                    Toast.makeText(requireContext(), "Failed to fetch weather data: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OpenWeatherResponse> call, @NonNull Throwable t)
            {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("WeatherFragment", "Network error: " + t.getMessage(), t);
            }
        });
    }

    private void updateCurrentWeather(OpenWeatherResponse weather)
    {
        currentLocation.setText(weather.getTimezone().split("/")[1]);

        currentTemp.setText(getString(R.string.temperature_format, (int) weather.getCurrent().getTemperature()));
        currentDescription.setText(weather.getCurrent().getWeather().get(0).getDescription());
        currentHumidity.setText(getString(R.string.humidity_format, weather.getCurrent().getHumidity()));
        currentWind.setText(getString(R.string.wind_format, weather.getCurrent().getWindSpeed()));
        currentPressure.setText(getString(R.string.pressure_format, weather.getCurrent().getPressure()));
    }

    private void updateHourlyForecast(OpenWeatherResponse weather)
    {
        List<OpenWeatherResponse.Hourly> nextSixHours = new ArrayList<>();
        long currentTime = System.currentTimeMillis() / 1000;

        for (OpenWeatherResponse.Hourly hour : weather.getHourly())
        {
            if (hour.getTimestamp() >= currentTime && hour.getTimestamp() < currentTime + 6 * 3600)
            {
                nextSixHours.add(hour);
            }
        }

        adapter.updateData(nextSixHours);
        Log.d("WeatherFragment", "Hourly forecast size: " + nextSixHours.size());
    }

    private class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder>
    {
        private List<OpenWeatherResponse.Hourly> hourlyData;

        HourlyForecastAdapter(List<OpenWeatherResponse.Hourly> hourlyData)
        {
            this.hourlyData = hourlyData;
        }

        void updateData(List<OpenWeatherResponse.Hourly> newData)
        {
            this.hourlyData = newData;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hourly_forecast, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
            OpenWeatherResponse.Hourly hour = hourlyData.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:00", Locale.getDefault());
            String time = sdf.format(new Date(hour.getTimestamp() * 1000));
            holder.time.setText(time);
            holder.temp.setText(holder.itemView.getContext().getString(R.string.temperature_format, (int) hour.getTemperature()));
            holder.description.setText(hour.getWeather().get(0).getDescription());
        }

        @Override
        public int getItemCount()
        {
            return hourlyData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView time, temp, description;

            ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                time = itemView.findViewById(R.id.hourly_time);
                temp = itemView.findViewById(R.id.hourly_temp);
                description = itemView.findViewById(R.id.hourly_description);
            }
        }
    }
}