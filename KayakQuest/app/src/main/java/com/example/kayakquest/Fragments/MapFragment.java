package com.example.kayakquest.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.kayakquest.Operations.SelectedPinViewModel;
import com.example.kayakquest.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback
{
    private GoogleMap googleMap;
    private SelectedPinViewModel viewModel;
    private static final String TAG = "MapFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(SelectedPinViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
        {
            mapFragment.getMapAsync(this);
        }
        else
        {
            Log.e(TAG, "Map fragment not found in layout");
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        this.googleMap = googleMap;

        List<MarkerData> markers = loadMarkersFromJson(requireContext());
        addMarkersToMap(markers);

        LatLng defaultLocation = new LatLng(35.227085, -80.843124);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));

        googleMap.setOnMarkerClickListener(marker -> {
            LatLng position = marker.getPosition();
            viewModel.setSelectedPin(position);
            Log.d(TAG, "Marker clicked at: " + position.latitude + "," + position.longitude);
            return false;
        });
    }

    private List<MarkerData> loadMarkersFromJson(Context context)
    {
        List<MarkerData> markers = new ArrayList<>();
        try (InputStream inputStream = context.getAssets().open("prepopulated_markers.json"))
        {
            Log.d(TAG, "Successfully opened prepopulated_markers.json");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalBytes = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }
            Log.d(TAG, "Read " + totalBytes + " bytes from JSON file");
            String json = outputStream.toString(StandardCharsets.UTF_8.name());
            Log.d(TAG, "JSON content: " + json);
            JSONArray jsonArray = new JSONArray(json);
            Log.d(TAG, "JSON array length: " + jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                double latitude = jsonObject.getDouble("latitude");
                double longitude = jsonObject.getDouble("longitude");
                Log.d(TAG, "Parsed marker: " + name + " at (" + latitude + "," + longitude + ")");
                markers.add(new MarkerData(name, latitude, longitude));
            }
        }
        catch (IOException e)
        {
            Log.e(TAG, "IOException loading markers: " + e.getMessage(), e);
        }
        catch (JSONException e)
        {
            Log.e(TAG, "JSONException parsing markers: " + e.getMessage(), e);
        }
        Log.d(TAG, "Returning " + markers.size() + " markers");
        return markers;
    }

    private void addMarkersToMap(List<MarkerData> markers) {
        if (googleMap == null) return;

        for (MarkerData marker : markers)
        {
            LatLng position = new LatLng(marker.getLatitude(), marker.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(marker.getName()));
        }
    }

    private static class MarkerData
    {
        private final String name;
        private final double latitude;
        private final double longitude;

        MarkerData(String name, double latitude, double longitude)
        {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        String getName()
        {
            return name;
        }

        double getLatitude()
        {
            return latitude;
        }

        double getLongitude()
        {
            return longitude;
        }
    }
}