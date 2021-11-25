package it.unimib.travelnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class TravelList extends AppCompatActivity {

    private Button mButtonNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_list);

        mButtonNew = findViewById(R.id.new_travel);

        mButtonNew.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewTravel.class);
        });
    }
}