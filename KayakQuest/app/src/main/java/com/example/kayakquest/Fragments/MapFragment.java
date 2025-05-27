package com.example.kayakquest.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.kayakquest.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback
{
    private GoogleMap googleMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null)
        {
            mapFragment.getMapAsync(this);
        }
        else
        {
            Toast.makeText(requireContext(), "Map fragment not found", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        this.googleMap = googleMap;
        LatLng defaultLocation = new LatLng(37.7749, -122.4194); // San Francisco
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));

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
}