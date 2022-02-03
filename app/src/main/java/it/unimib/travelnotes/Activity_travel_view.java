package it.unimib.travelnotes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unimib.travelnotes.databinding.ActivityTravelView2Binding;
import it.unimib.travelnotes.ui.flight.FlightViewModel;

public class Activity_travel_view extends AppCompatActivity {
    private String viaggioId;
    private String viaggio_id ;
    private ActivityTravelView2Binding binding;
    BottomNavigationView bottomNavigation;
    FlightViewModel mFlightViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_view2);

        try{
            viaggio_id = (String) getIntent().getExtras().get(viaggioId);
        }catch (Exception e){
            viaggio_id = null;
        }
        /*if(viaggio_id == null){
            Intent intent = new Intent(getApplicationContext(), TravelList.class);
        }*/
        viaggio_id = "-MtA7mKtdZODJR98_3hH";
        SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
        sharedPreferencesProvider.setSelectedViaggioId(viaggio_id);

        //Logic to intercept te Intent and its data
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        bottomNavigation = findViewById(R.id.nav_view);

        // Logic to manage the behavior of the BottomNavigationView and Toolbar
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_travel_view2);
        NavController navController = navHostFragment.getNavController();

        //for the bottom navigation
        NavigationUI.setupWithNavController(bottomNavigation, navController);

        /*toolbar
        Toolbar toolbar = findViewById(R.id.);
        setSupportActionBar(toolbar);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.country_news, R.id.topic_news, R.id.favorites).build();*/



    }


}