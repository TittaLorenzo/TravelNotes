package it.unimib.travelnotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import it.unimib.travelnotes.autentication.LoginActivity;
import it.unimib.travelnotes.autentication.RegisterActivity;
import it.unimib.travelnotes.ui.newactivityevent.NewActivityEvent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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