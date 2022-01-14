package it.unimib.travelnotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import it.unimib.travelnotes.autentication.LoginActivity;
import it.unimib.travelnotes.autentication.RegisterActivity;
import it.unimib.travelnotes.ui.newactivityevent.NewActivityEvent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferencesProvider mSharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
        String userId = mSharedPreferencesProvider.getSharedUserId();

        if (userId != null) {
            startActivity(new Intent(this, TravelList.class));
        }


        setContentView(R.layout.activity_main);


        //bottone login che porta alla lista viaggi
        final Button mButtonNext = findViewById(R.id.L_button);
        mButtonNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, TravelList.class);
            startActivity(intent);
        });

        //bottone fittizio nuova attività che porta alla schermata di aggiunta attività
        final Button nButtonNext = findViewById(R.id.iNuovaAttivitaButton);
        nButtonNext.setOnClickListener(v -> {
            Intent i = new Intent(this, NewActivityEvent.class);
            startActivity(i);
        });

        //bottone fittizio nuova attività che porta alla schermata di aggiunta attività
        final Button attivitaUno = findViewById(R.id.attivitaUno);
        attivitaUno.setOnClickListener(v -> {
            Intent i = new Intent(this, NewActivityEvent.class);
            i.putExtra("viaggioId", "-MtA7mKtdZODJR98_3hH");
            i.putExtra("attivitaId", "-MtABb5CDgpuMyffg8bd");
            startActivity(i);
        });


        //bottone fittizio schermata di login
        final Button loginPage = findViewById(R.id.loginPageButton);
        loginPage.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        });

        //bottone fittizio registrazione
        final Button registerPage = findViewById(R.id.registerPageButton);
        registerPage.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });

    }
}