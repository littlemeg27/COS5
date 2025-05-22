package com.example.kayakquest.Operations;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.kayakquest.Fragments.FloatPlanFragment;
import com.example.kayakquest.Fragments.MapFragment;
import com.example.kayakquest.Fragments.SettingsFragment;
import com.example.kayakquest.Fragments.SignInFragment;
import com.example.kayakquest.Fragments.WeatherFragment;
import com.example.kayakquest.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Log Firebase initialization status
        try
        {
            Log.d("MainActivity", "FirebaseApp initialized: " + FirebaseApp.getInstance().getName());
        }
        catch (Exception e)
        {
            Log.e("MainActivity", "Firebase not initialized", e);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_signin)
            {
                selectedFragment = new SignInFragment();
            }
            else if (itemId == R.id.nav_map)
            {
                selectedFragment = new MapFragment();
            }
            else if (itemId == R.id.nav_float_plan)
            {
                selectedFragment = new FloatPlanFragment();
            }
            else if (itemId == R.id.nav_weather)
            {
                selectedFragment = new WeatherFragment();
            }
            else if (itemId == R.id.nav_settings)
            {
                selectedFragment = new SettingsFragment();
            }
            if (selectedFragment != null)
            {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SignInFragment())
                    .commit();
        }
    }
}