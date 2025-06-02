package com.example.kayakquest.Operations;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.maps.model.LatLng;

public class SelectedPinViewModel extends ViewModel
{
    private final MutableLiveData<LatLng> selectedPin = new MutableLiveData<>();

    public void setSelectedPin(LatLng latLng)
    {
        selectedPin.setValue(latLng);
    }

    public LiveData<LatLng> getSelectedPin()
    {
        return selectedPin;
    }
}
