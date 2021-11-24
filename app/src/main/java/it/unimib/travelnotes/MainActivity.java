package it.unimib.travelnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bottone login che porta alla lista viaggi
        final Button mButtonNext = findViewById(R.id.L_button);
        mButtonNext.setOnClickListener(v -> {
            // inserire TravelList e rimuovere commento
            Intent intent = new Intent(this, NewTravel.class);
            startActivity(intent);
        });
    }
}