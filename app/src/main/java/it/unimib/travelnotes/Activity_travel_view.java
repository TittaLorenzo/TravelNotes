package it.unimib.travelnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.List;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.autentication.LoginActivity;
import it.unimib.travelnotes.databinding.ActivityTravelView2Binding;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;
import it.unimib.travelnotes.ui.flight.FlightFragment;
import it.unimib.travelnotes.ui.flight.FlightViewModel;

public class Activity_travel_view extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String viaggioId;
    private String viaggio_id; ;
    private ActivityTravelView2Binding binding;
    BottomNavigationView bottomNavigation;
    FlightViewModel mFlightViewModel;
    private List<Attivita> attivitaList = new ArrayList<Attivita>();
    private FlightFragment flightFragment = new FlightFragment();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_view2);
        mAuth = FirebaseAuth.getInstance();


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
        Bundle bundle = new Bundle();
        bundle.putString("viaggioId", viaggio_id);
        flightFragment.setArguments(bundle);


    }

    //public List<Attivita> getAttivitaList(){return this.attivitaList; }
    public void onChange (List<Attivita> lista){this.attivitaList = lista;}


    // Oprions Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutItemMenu:
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(this, "Logout effettuato", Toast.LENGTH_SHORT).show();

                    SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
                    sharedPreferencesProvider.setSharedUserId(null);

                    //delateAll RoomDb

                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    Toast.makeText(this, "Nessun utente loggato", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.changePwItemMenu:
                EditText newPassword = new EditText(this);
                AlertDialog.Builder changePwDialog = new AlertDialog.Builder(this);
                changePwDialog.setTitle("Cambia password?");
                changePwDialog.setMessage("Inserisci la tua nuova password.");
                changePwDialog.setView(newPassword);

                changePwDialog.setPositiveButton("Invia", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newPw = newPassword.getText().toString();
                        mAuth.getCurrentUser().updatePassword(newPw).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(@NonNull Void unused) {
                                Toast.makeText(Activity_travel_view.this, "La password è stata cambiata", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Activity_travel_view.this, "Errore! Password non cambiata", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                changePwDialog.setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close Dialog
                    }
                });
                changePwDialog.create().show();

                break;

            case R.id.RefreshItemMenu:
                //refresh method
                break;
            case R.id.chUsernamePwItemMenu:
                EditText newUsername = new EditText(this);
                AlertDialog.Builder changeUnDialog = new AlertDialog.Builder(this);
                changeUnDialog.setTitle("Cambia username?");
                changeUnDialog.setMessage("Inserisci il tuo nuovo username.");
                changeUnDialog.setView(newUsername);

                changeUnDialog.setPositiveButton("Invia", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newUn = newUsername.getText().toString();

                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(newUn)
                                .build();
                        assert user != null;
                        user.updateProfile((profileUpdates));

                        Utente u = new Utente(user.getUid(), user.getEmail(), newUn);
                        ITravelRepository travelRepository = new TravelRepository(getApplication());
                        travelRepository.pushNuovoUtente(u);
                    }
                });
                changeUnDialog.setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close Dialog
                    }
                });
                changeUnDialog.create().show();

                break;
        }
        return true;
    }


}