package com.example.kayakquest.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.kayakquest.Operations.MarkerData;
import com.example.kayakquest.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MapFragment extends Fragment implements OnMapReadyCallback
{
    private GoogleMap googleMap;
    private List<MarkerData> markerList;
    private static final String PREFS_NAME = "MapPrefs";
    private static final String MARKERS_KEY = "Markers";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        markerList = new ArrayList<>();
        loadMarkers();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null)
        {
            Log.d("MapFragment", "Map fragment found, requesting map async");
            mapFragment.getMapAsync(this);
        }
        else
        {
            Log.e("MapFragment", "Map fragment not found");
            Toast.makeText(requireContext(), "Map fragment not found", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        Log.d("MapFragment", "Map ready, setting default location");

        LatLng defaultLocation = new LatLng(35.227085, -80.843124);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
        Log.d("MapFragment", "Camera moved to: " + defaultLocation.latitude + ", " + defaultLocation.longitude);

        List<MarkerData> prePopulatedMarkers = getPrePopulatedMarkers();

        for (MarkerData markerData : prePopulatedMarkers)
        {
            googleMap.addMarker(new MarkerOptions()
                    .position(markerData.getLatLng())
                    .title(markerData.title)
                    .snippet(markerData.snippet));
        }
        Log.d("MapFragment", "Added " + prePopulatedMarkers.size() + " pre-populated markers");

        for (MarkerData markerData : markerList)
        {
            googleMap.addMarker(new MarkerOptions()
                    .position(markerData.getLatLng())
                    .title(markerData.title)
                    .snippet(markerData.snippet));
        }
        Log.d("MapFragment", "Loaded " + markerList.size() + " user-added markers");

        googleMap.setOnMapLongClickListener(latLng ->
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Add Person Pin");

            final EditText input = new EditText(requireContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) ->
            {
                String personName = input.getText().toString().trim();

                if (!personName.isEmpty())
                {
                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .title(personName)
                            .snippet("Added: " + formattedDate);
                    googleMap.addMarker(markerOptions);

                    MarkerData markerData = new MarkerData(latLng.latitude, latLng.longitude, personName, "Added: " + formattedDate);
                    markerList.add(markerData);
                    saveMarkers();

                    Toast.makeText(requireContext(), "Person pin added for " + personName, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
        {
            @Override
            public View getInfoWindow(@NonNull Marker marker)
            {
                return null;
            }

            @Override
            public View getInfoContents(@NonNull Marker marker)
            {
                View view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_info_window, null);
                TextView title = view.findViewById(R.id.title);
                TextView snippet = view.findViewById(R.id.snippet);
                title.setText(marker.getTitle());
                snippet.setText(marker.getSnippet());
                return view;
            }
        });

        googleMap.setOnMarkerClickListener(marker ->
        {
            marker.showInfoWindow();
            return true;
        });
    }

    private List<MarkerData> getPrePopulatedMarkers()
    {
        List<MarkerData> prePopulated = new ArrayList<>();
        try
        {
            InputStream inputStream = getResources().openRawResource(R.raw.prepopulated_markers);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<MarkerData>>() {}.getType();
            prePopulated = gson.fromJson(json, type);
            Log.d("MapFragment", "Loaded " + prePopulated.size() + " pre-populated markers from JSON");
        }
        catch (IOException e)
        {
            Log.e("MapFragment", "Error loading pre-populated markers", e);
        }
        return prePopulated;
    }

    private void saveMarkers()
    {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(markerList);
        editor.putString(MARKERS_KEY, json);
        editor.apply();
        Log.d("MapFragment", "Saved " + markerList.size() + " user-added markers");
    }

    private void loadMarkers()
    {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(MARKERS_KEY, null);
        Type type = new TypeToken<ArrayList<MarkerData>>() {}.getType();
        if (json != null)
        {
            markerList = gson.fromJson(json, type);
        }
        if (markerList == null)
        {
            markerList = new ArrayList<>();
        }
        Log.d("MapFragment", "Loaded " + markerList.size() + " user-added markers from SharedPreferences");
    }
}