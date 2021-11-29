package it.unimib.travelnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.unimib.travelnotes.Model.Viaggio;

public class TravelList extends AppCompatActivity implements View.OnClickListener {

//    private List<Viaggio> travelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_list);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Viaggio[] viaggio = new Viaggio[]{
                new Viaggio("Milano", "Parigi", "Parigi", "Milano", 90, 90),
                new Viaggio("Roma", "Berlino", "Berlino", "Roma", 90, 90),
                new Viaggio("Napoli", "Palermo", "Palermo", "Napoli", 90, 90),
                new Viaggio("Parigi", "Tokyo", "Tokyo", "Parigi", 90, 90),
                new Viaggio("Los Angeles", "New York", "Los Angeles", "New York", 90, 90),
                new Viaggio("Berlino", "Tokyo", "Tokyo", "Berlino", 90, 90),
                new Viaggio("Madrid", "Porto", "Porto", "Madrid", 90, 90),
                new Viaggio("Lisbona", "Barcellona", "Barcellona", "Lisbona", 90, 90),
                new Viaggio("Milano", "Roma", "Roma", "Milano", 90, 90),
                new Viaggio("Los Angeles", "New York", "Los Angeles", "New York", 90, 90),
                new Viaggio("Milano", "Parigi", "Parigi", "Milano", 90, 90),
                new Viaggio("Roma", "Berlino", "Berlino", "Roma", 90, 90),
                new Viaggio("Napoli", "Palermo", "Palermo", "Napoli", 90, 90),
                new Viaggio("Parigi", "Tokyo", "Tokyo", "Parigi", 90, 90),
                new Viaggio("Los Angeles", "New York", "Los Angeles", "New York", 90, 90),
                new Viaggio("Berlino", "Tokyo", "Tokyo", "Berlino", 90, 90),
                new Viaggio("Madrid", "Porto", "Porto", "Madrid", 90, 90),
                new Viaggio("Lisbona", "Barcellona", "Barcellona", "Lisbona", 90, 90),
                new Viaggio("Milano", "Roma", "Roma", "Milano", 90, 90),
                new Viaggio("Los Angeles", "New York", "Los Angeles", "New York", 90, 90),
        };

        TravelAdapter travelAdapter = new TravelAdapter(viaggio, TravelList.this);
        recyclerView.setAdapter(travelAdapter);



        final Button mButtonNext = findViewById(R.id.new_travel);
        mButtonNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewTravel.class);
            startActivity(intent);
        });

    }

    @Override
    public void onClick(View v) {

    }
}