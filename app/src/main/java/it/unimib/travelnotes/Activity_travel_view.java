/*package it.unimib.travelnotes;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import it.unimib.travelnotes.databinding.ActivityTravelView2Binding;

public class Activity_travel_view extends AppCompatActivity {

    private ActivityTravelView2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

}*/
//rimettere a posto
package it.unimib.travelnotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unimib.travelnotes.databinding.ActivityTravelView2Binding;

public class Activity_travel_view extends AppCompatActivity {

    private ActivityTravelView2Binding binding;

    TextView  partenza,arrivo/*, ritornoDa, ritornoA*/;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityTravelView2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        partenza=findViewById(R.id.NT_andataDa);
        arrivo=findViewById(R.id.NT_andataA);
        /*ritornoDa=findViewById(R.id.NT_ritornoDa);
        ritornoA=findViewById(R.id.NT_ritornoA);*/

        Intent intent=getIntent();

        partenza.setText(intent.getStringExtra("partenza"));
        arrivo.setText(intent.getStringExtra("arrivo"));
        /*ritornoDa.setText(intent.getStringExtra("partenzaR"));
        ritornoA.setText(intent.getStringExtra("arrivoR"));*/


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