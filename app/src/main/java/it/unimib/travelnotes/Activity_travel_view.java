package it.unimib.travelnotes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unimib.travelnotes.databinding.ActivityTravelView2Binding;

public class Activity_travel_view extends AppCompatActivity {

    private String viaggio_id ;
    private ActivityTravelView2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            viaggio_id = (String) getIntent().getExtras().get("viaggioId");
        }catch (Exception e){
            viaggio_id = null;
        }
        if(viaggio_id == null){
            Intent intent = new Intent(getApplicationContext(), TravelList.class);
        }

        SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
        sharedPreferencesProvider.setSelectedViaggioId(viaggio_id);

        binding = ActivityTravelView2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_travel_view2);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }

}