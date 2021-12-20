package it.unimib.travelnotes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import it.unimib.travelnotes.Model.TravelResponse;
import it.unimib.travelnotes.Model.Viaggio;


public class TravelList extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TravelFragment";

    private Viaggio[] travelArray ;
    private List<Viaggio> travelList;
    private List<Viaggio> mTravelListJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        /*Viaggio[] viaggio = new Viaggio[]{
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
        };*/

        TravelResponse travelListWithJsonReader = readJsonFileWithGson();
        travelArray = travelListWithJsonReader.getViaggi();

        for (int i = 0; i < travelArray.length; i++) {
            Log.d(TAG, "Gson: " + travelArray[i]);
        }



        /*
        *
        * FARE ORDINAMENTO IN BASE ALLA DATA
        *
        * */

        TravelAdapter travelAdapter = new TravelAdapter(travelArray, TravelList.this);
        recyclerView.setAdapter(travelAdapter);


        final Button mButtonNext = findViewById(R.id.new_travel);
        mButtonNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewTravel.class);
            startActivity(intent);
        });

    }



    private TravelResponse readJsonFileWithGson() {

        TravelResponse viaggi = null;

        try {
            InputStream inputStream = this.getAssets().open("viaggi.json");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            viaggi = new Gson().fromJson(bufferedReader, TravelResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return viaggi;
    }




    @Override
    public void onClick(View v) {

    }


}



