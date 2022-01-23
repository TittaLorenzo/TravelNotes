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
            finish();
        }

        setContentView(R.layout.activity_main);

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