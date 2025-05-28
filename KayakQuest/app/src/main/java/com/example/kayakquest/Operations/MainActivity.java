package com.example.kayakquest.Operations;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.kayakquest.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            Log.d("MainActivity", "FirebaseApp initialized: " + FirebaseApp.getInstance().getName());
        }
        catch (Exception e)
        {
            Log.e("MainActivity", "Firebase not initialized", e);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (toolbar == null)
        {
            Log.e("MainActivity", "Toolbar not found");
            return;
        }
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        if (navHostFragment == null)
        {
            Log.e("MainActivity", "NavHostFragment not found");
            return;
        }

        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        if (bottomNav == null)
        {
            Log.e("MainActivity", "BottomNavigationView not found");
            return;
        }

        AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
                R.id.signInFragment,
                R.id.mapFragment,
                R.id.floatPlanFragment,
                R.id.weatherFragment,
                R.id.settingsFragment
        ).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
        NavigationUI.setupWithNavController(bottomNav, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) ->
        {
            Log.d("MainActivity", "Navigated to: " + destination.getLabel());
        });
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        if (navHostFragment == null)
        {
            Log.e("MainActivity", "NavHostFragment not found in onSupportNavigateUp");
            return super.onSupportNavigateUp();
        }
        NavController navController = navHostFragment.getNavController();
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}