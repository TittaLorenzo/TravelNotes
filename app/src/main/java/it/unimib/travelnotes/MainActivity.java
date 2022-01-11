package it.unimib.travelnotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import it.unimib.travelnotes.autentication.LoginActivity;
import it.unimib.travelnotes.autentication.RegisterActivity;
import it.unimib.travelnotes.ui.newactivityevent.NewActivityEvent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.shared_userid_key);
        String userId = sharedPref.getString(getString(R.string.shared_userid_key), defaultValue);
        if (userId != null) {
            startActivity(new Intent(this, TravelList.class));
        }


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
            i.putExtra("idAttivita", 1);
            i.putExtra("viaggioId", 1);
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