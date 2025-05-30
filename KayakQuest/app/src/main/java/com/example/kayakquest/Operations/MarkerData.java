package com.example.kayakquest.Operations;

import com.google.android.gms.maps.model.LatLng;

public class MarkerData
{
    public double latitude;
    public double longitude;
    public String title;
    public String snippet;

    public MarkerData(double latitude, double longitude, String title, String snippet)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.snippet = snippet;
    }

    public LatLng getLatLng()
    {
        return new LatLng(latitude, longitude);
    }
}